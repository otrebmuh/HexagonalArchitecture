package com.example.hexagonalorders.domain.port.out;

import com.example.hexagonalorders.domain.model.OutboxMessage;

import java.util.List;
import java.util.UUID;

/**
 * Output port defining the contract for outbox message persistence.
 * This interface is part of the domain layer and defines how
 * the application core interacts with the outbox persistence mechanism.
 */
public interface OutboxRepository {
    /**
     * Saves an outbox message
     * @param message the outbox message to save
     * @return the saved outbox message
     */
    OutboxMessage save(OutboxMessage message);

    /**
     * Finds pending outbox messages up to the specified limit
     * @param limit maximum number of messages to retrieve
     * @return list of pending outbox messages
     */
    List<OutboxMessage> findPending(int limit);

    /**
     * Marks an outbox message as processed
     * @param id the ID of the outbox message
     */
    void markProcessed(UUID id);

    /**
     * Marks an outbox message as failed
     * @param id the ID of the outbox message
     */
    void markFailed(UUID id);
} 