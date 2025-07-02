package com.example.hexagonalorders.infrastructure.out.event;

import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.port.out.MessagePublisher;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Component responsible for processing outbox messages and publishing them to external systems.
 * This implements the outbox pattern for reliable message delivery.
 */
@Component
public class OutboxProcessor {

    private static final Logger log = LoggerFactory.getLogger(OutboxProcessor.class);
    private static final int BATCH_SIZE = 10;

    private final OutboxRepository outboxRepository;
    private final MessagePublisher messagePublisher;

    public OutboxProcessor(OutboxRepository outboxRepository, MessagePublisher messagePublisher) {
        this.outboxRepository = outboxRepository;
        this.messagePublisher = messagePublisher;
    }

    /**
     * Scheduled task that processes pending outbox messages.
     * This method runs at a fixed delay defined by the outbox.poll.ms property (defaults to 1000ms).
     */
    @Scheduled(fixedDelayString = "${outbox.poll.ms:1000}")
    public void processOutboxMessages() {
        List<OutboxMessage> pendingMessages = outboxRepository.findPending(BATCH_SIZE);
        
        if (!pendingMessages.isEmpty()) {
            log.info("Processing {} outbox messages", pendingMessages.size());
            
            pendingMessages.forEach(this::processMessage);
        }
    }

    /**
     * Processes a single outbox message.
     * This method is transactional to ensure consistent state.
     */
    @Transactional
    public void processMessage(OutboxMessage message) {
        try {
            log.debug("Publishing message: {}", message.id());
            
            // Publish the message to the external system
            messagePublisher.publish(
                message.aggregateType() + "." + message.eventType(),
                message.payload()
            );
            
            // Mark the message as processed
            outboxRepository.markProcessed(message.id());
            
            log.debug("Message published successfully: {}", message.id());
        } catch (Exception e) {
            log.error("Failed to process outbox message: {}", message.id(), e);
            
            // Mark the message as failed
            outboxRepository.markFailed(message.id());
        }
    }
} 