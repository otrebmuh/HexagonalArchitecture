package com.example.hexagonalorders.application.handler;

import com.example.hexagonalorders.application.event.OrderConfirmedIntegrationEvent;
import com.example.hexagonalorders.domain.event.DomainEvent;
import com.example.hexagonalorders.domain.event.OrderConfirmedEvent;
import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Event handler for domain events that need to be mapped to integration events.
 * This class centralizes the business logic for deciding which domain events
 * should trigger external integration events.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventHandler {
    
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    
    /**
     * Handles OrderConfirmedEvent by creating and persisting the corresponding integration event.
     * This method is automatically called by Spring's event system when an OrderConfirmedEvent is published.
     * 
     * @param event the OrderConfirmedEvent that was raised
     */
    @EventListener
    public void handleOrderConfirmed(OrderConfirmedEvent event) {
        log.info("🎉 DOMAIN EVENT HANDLER: Handling OrderConfirmedEvent for order: {}", event.getOrderNumber());
        
        try {
            // Create integration event
            OrderConfirmedIntegrationEvent integrationEvent = 
                new OrderConfirmedIntegrationEvent(event.getOrderNumber());
            
            // Persist to outbox for reliable delivery
            persistToOutbox(integrationEvent, "Order", event.getOrderNumber().value());
            
            log.info("✅ DOMAIN EVENT HANDLER: Successfully created integration event for order confirmation: {}", 
                     event.getOrderNumber());
        } catch (Exception e) {
            log.error("❌ DOMAIN EVENT HANDLER: Failed to handle OrderConfirmedEvent for order: {}", 
                     event.getOrderNumber(), e);
            throw new RuntimeException("Failed to process order confirmation integration event", e);
        }
    }
    
    /**
     * Generic handler for domain events that don't require integration events.
     * This method logs the event for debugging purposes but doesn't create integration events.
     * 
     * @param event any domain event that doesn't have a specific handler
     */
    @EventListener
    public void handleGenericDomainEvent(DomainEvent event) {
        log.info("📝 DOMAIN EVENT HANDLER: Received domain event: {} - no integration event required", 
                 event.getClass().getSimpleName());
    }
    
    /**
     * Persists an integration event to the outbox.
     * 
     * @param event the integration event to persist
     * @param aggregateType the type of aggregate that produced the event
     * @param aggregateId the identifier of the aggregate
     */
    private void persistToOutbox(Object event, String aggregateType, String aggregateId) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            String eventType = event.getClass().getSimpleName();
            
            // Generate a deterministic UUID based on the aggregateId string
            // This ensures the same aggregate always gets the same UUID
            UUID uuid = UUID.nameUUIDFromBytes(aggregateId.getBytes());
            
            OutboxMessage outboxMessage = OutboxMessage.createPendingMessage(
                aggregateType,
                uuid,
                eventType,
                payload
            );
            
            outboxRepository.save(outboxMessage);
            log.debug("Persisted integration event to outbox: {} for aggregate: {}", 
                     eventType, aggregateId);
        } catch (Exception e) {
            log.error("Failed to persist integration event to outbox for aggregate: {}", 
                     aggregateId, e);
            throw new RuntimeException("Failed to persist event to outbox", e);
        }
    }
} 