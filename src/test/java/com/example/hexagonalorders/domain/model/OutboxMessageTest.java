package com.example.hexagonalorders.domain.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OutboxMessageTest {

    @Test
    void shouldCreatePendingMessageWithValidParameters() {
        // Given
        String aggregateType = "Order";
        UUID aggregateId = UUID.randomUUID();
        String eventType = "OrderCreatedEvent";
        String payload = "{\"orderId\":1,\"orderNumber\":\"ORD-001\"}";

        // When
        OutboxMessage message = OutboxMessage.createPendingMessage(aggregateType, aggregateId, eventType, payload);

        // Then
        assertNotNull(message);
        assertNotNull(message.id());
        assertEquals(aggregateType, message.aggregateType());
        assertEquals(aggregateId, message.aggregateId());
        assertEquals(eventType, message.eventType());
        assertEquals(payload, message.payload());
        assertEquals(OutboxStatus.PENDING, message.status());
        assertNotNull(message.createdAt());
        assertNull(message.processedAt());
    }

    @Test
    void shouldCreatePendingMessageWithNullAggregateType() {
        // Given
        UUID aggregateId = UUID.randomUUID();
        String eventType = "OrderCreatedEvent";
        String payload = "{\"orderId\":1}";

        // When
        OutboxMessage message = OutboxMessage.createPendingMessage(null, aggregateId, eventType, payload);

        // Then
        assertNotNull(message);
        assertNull(message.aggregateType());
        assertEquals(aggregateId, message.aggregateId());
        assertEquals(eventType, message.eventType());
        assertEquals(payload, message.payload());
        assertEquals(OutboxStatus.PENDING, message.status());
    }

    @Test
    void shouldCreatePendingMessageWithNullEventType() {
        // Given
        String aggregateType = "Order";
        UUID aggregateId = UUID.randomUUID();
        String payload = "{\"orderId\":1}";

        // When
        OutboxMessage message = OutboxMessage.createPendingMessage(aggregateType, aggregateId, null, payload);

        // Then
        assertNotNull(message);
        assertEquals(aggregateType, message.aggregateType());
        assertEquals(aggregateId, message.aggregateId());
        assertNull(message.eventType());
        assertEquals(payload, message.payload());
        assertEquals(OutboxStatus.PENDING, message.status());
    }

    @Test
    void shouldCreatePendingMessageWithNullPayload() {
        // Given
        String aggregateType = "Order";
        UUID aggregateId = UUID.randomUUID();
        String eventType = "OrderCreatedEvent";

        // When
        OutboxMessage message = OutboxMessage.createPendingMessage(aggregateType, aggregateId, eventType, null);

        // Then
        assertNotNull(message);
        assertEquals(aggregateType, message.aggregateType());
        assertEquals(aggregateId, message.aggregateId());
        assertEquals(eventType, message.eventType());
        assertNull(message.payload());
        assertEquals(OutboxStatus.PENDING, message.status());
    }

    @Test
    void shouldMarkMessageAsProcessed() {
        // Given
        UUID id = UUID.randomUUID();
        String aggregateType = "Order";
        UUID aggregateId = UUID.randomUUID();
        String eventType = "OrderCreatedEvent";
        String payload = "{\"orderId\":1}";
        Instant createdAt = Instant.now();
        
        OutboxMessage originalMessage = new OutboxMessage(
            id, aggregateType, aggregateId, eventType, payload, 
            OutboxStatus.PENDING, createdAt, null
        );

        // When
        OutboxMessage processedMessage = originalMessage.markAsProcessed();

        // Then
        assertNotNull(processedMessage);
        assertEquals(id, processedMessage.id());
        assertEquals(aggregateType, processedMessage.aggregateType());
        assertEquals(aggregateId, processedMessage.aggregateId());
        assertEquals(eventType, processedMessage.eventType());
        assertEquals(payload, processedMessage.payload());
        assertEquals(OutboxStatus.PROCESSED, processedMessage.status());
        assertEquals(createdAt, processedMessage.createdAt());
        assertNotNull(processedMessage.processedAt());
        assertTrue(processedMessage.processedAt().isAfter(createdAt) || 
                   processedMessage.processedAt().equals(createdAt));
    }

    @Test
    void shouldMarkMessageAsFailed() {
        // Given
        UUID id = UUID.randomUUID();
        String aggregateType = "Order";
        UUID aggregateId = UUID.randomUUID();
        String eventType = "OrderCreatedEvent";
        String payload = "{\"orderId\":1}";
        Instant createdAt = Instant.now();
        
        OutboxMessage originalMessage = new OutboxMessage(
            id, aggregateType, aggregateId, eventType, payload, 
            OutboxStatus.PENDING, createdAt, null
        );

        // When
        OutboxMessage failedMessage = originalMessage.markAsFailed();

        // Then
        assertNotNull(failedMessage);
        assertEquals(id, failedMessage.id());
        assertEquals(aggregateType, failedMessage.aggregateType());
        assertEquals(aggregateId, failedMessage.aggregateId());
        assertEquals(eventType, failedMessage.eventType());
        assertEquals(payload, failedMessage.payload());
        assertEquals(OutboxStatus.FAILED, failedMessage.status());
        assertEquals(createdAt, failedMessage.createdAt());
        assertNotNull(failedMessage.processedAt());
        assertTrue(failedMessage.processedAt().isAfter(createdAt) || 
                   failedMessage.processedAt().equals(createdAt));
    }

    @Test
    void shouldCreateOutboxMessageWithAllParameters() {
        // Given
        UUID id = UUID.randomUUID();
        String aggregateType = "Order";
        UUID aggregateId = UUID.randomUUID();
        String eventType = "OrderCreatedEvent";
        String payload = "{\"orderId\":1}";
        OutboxStatus status = OutboxStatus.PENDING;
        Instant createdAt = Instant.now();
        Instant processedAt = Instant.now().plusSeconds(60);

        // When
        OutboxMessage message = new OutboxMessage(
            id, aggregateType, aggregateId, eventType, payload, status, createdAt, processedAt
        );

        // Then
        assertNotNull(message);
        assertEquals(id, message.id());
        assertEquals(aggregateType, message.aggregateType());
        assertEquals(aggregateId, message.aggregateId());
        assertEquals(eventType, message.eventType());
        assertEquals(payload, message.payload());
        assertEquals(status, message.status());
        assertEquals(createdAt, message.createdAt());
        assertEquals(processedAt, message.processedAt());
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreEqual() {
        // Given
        UUID id = UUID.randomUUID();
        String aggregateType = "Order";
        UUID aggregateId = UUID.randomUUID();
        String eventType = "OrderCreatedEvent";
        String payload = "{\"orderId\":1}";
        OutboxStatus status = OutboxStatus.PENDING;
        Instant createdAt = Instant.now();
        Instant processedAt = Instant.now().plusSeconds(60);

        OutboxMessage message1 = new OutboxMessage(
            id, aggregateType, aggregateId, eventType, payload, status, createdAt, processedAt
        );
        OutboxMessage message2 = new OutboxMessage(
            id, aggregateType, aggregateId, eventType, payload, status, createdAt, processedAt
        );

        // When & Then
        assertEquals(message1, message2);
        assertEquals(message1.hashCode(), message2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenIdsAreDifferent() {
        // Given
        String aggregateType = "Order";
        UUID aggregateId = UUID.randomUUID();
        String eventType = "OrderCreatedEvent";
        String payload = "{\"orderId\":1}";
        OutboxStatus status = OutboxStatus.PENDING;
        Instant createdAt = Instant.now();
        Instant processedAt = Instant.now().plusSeconds(60);

        OutboxMessage message1 = new OutboxMessage(
            UUID.randomUUID(), aggregateType, aggregateId, eventType, payload, status, createdAt, processedAt
        );
        OutboxMessage message2 = new OutboxMessage(
            UUID.randomUUID(), aggregateType, aggregateId, eventType, payload, status, createdAt, processedAt
        );

        // When & Then
        assertNotEquals(message1, message2);
    }

    @Test
    void shouldNotBeEqualWhenComparedWithDifferentType() {
        // Given
        OutboxMessage message = OutboxMessage.createPendingMessage("Order", UUID.randomUUID(), "Event", "payload");
        String differentType = "not a message";

        // When & Then
        assertNotEquals(message, differentType);
    }
} 