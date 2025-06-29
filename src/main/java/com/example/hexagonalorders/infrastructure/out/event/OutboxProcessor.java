package com.example.hexagonalorders.infrastructure.out.event;

import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.port.out.MessagePublisher;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxProcessor {
    
    private static final Logger log = LoggerFactory.getLogger(OutboxProcessor.class);
    
    private final OutboxRepository outboxRepository;
    private final MessagePublisher messagePublisher;
    
    @Value("${outbox.batch.size:10}")
    private int batchSize;
    
    public OutboxProcessor(OutboxRepository outboxRepository, MessagePublisher messagePublisher) {
        this.outboxRepository = outboxRepository;
        this.messagePublisher = messagePublisher;
    }
    
    @Scheduled(fixedDelayString = "${outbox.poll.ms:1000}")
    @Transactional
    public void processPendingMessages() {
        try {
            log.debug("Starting to process pending outbox messages");
            
            // Extrae una página de mensajes con estado PENDING
            List<OutboxMessage> pendingMessages = outboxRepository.findPending(batchSize);
            
            if (pendingMessages.isEmpty()) {
                log.debug("No pending messages to process");
                return;
            }
            
            log.info("Processing {} pending messages", pendingMessages.size());
            
            for (OutboxMessage message : pendingMessages) {
                try {
                    // Publica el mensaje
                    messagePublisher.publish(message.eventType(), message.payload());
                    
                    // Actualiza el estado a PROCESSED
                    outboxRepository.markProcessed(message.id());
                    
                    log.debug("Successfully processed message with id: {}", message.id());
                    
                } catch (Exception e) {
                    log.error("Failed to process message with id: {}", message.id(), e);
                    
                    // Actualiza el estado a FAILED
                    outboxRepository.markFailed(message.id());
                }
            }
            
            log.debug("Finished processing pending outbox messages");
            
        } catch (Exception e) {
            log.error("Error in outbox processor", e);
        }
    }
} 