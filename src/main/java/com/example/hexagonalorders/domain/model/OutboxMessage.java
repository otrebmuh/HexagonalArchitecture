package com.example.hexagonalorders.domain.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain model representing an outbox message.
 * This is a pure domain object with no infrastructure dependencies.
 * It represents a message in the outbox pattern for reliable event publishing.
 */
public record OutboxMessage(
    UUID id,
    String aggregateType,
    UUID aggregateId,
    String eventType,
    String payload,
    OutboxStatus status,
    Instant createdAt,
    Instant processedAt
) {
    /**
     * Creates a new pending outbox message.
     */
    public static OutboxMessage createPendingMessage(
            String aggregateType,
            UUID aggregateId,
            String eventType,
            String payload) {
        return new OutboxMessage(
                UUID.randomUUID(),
                aggregateType,
                aggregateId,
                eventType,
                payload,
                OutboxStatus.PENDING,
                Instant.now(),
                null
        );
    }

    /**
     * Returns a new instance with status set to PROCESSED and processedAt set to now.
     */
    public OutboxMessage markAsProcessed() {
        return new OutboxMessage(
                this.id,
                this.aggregateType,
                this.aggregateId,
                this.eventType,
                this.payload,
                OutboxStatus.PROCESSED,
                this.createdAt,
                Instant.now()
        );
    }

    /**
     * Returns a new instance with status set to FAILED.
     */
    public OutboxMessage markAsFailed() {
        return new OutboxMessage(
                this.id,
                this.aggregateType,
                this.aggregateId,
                this.eventType,
                this.payload,
                OutboxStatus.FAILED,
                this.createdAt,
                Instant.now()
        );
    }
} 