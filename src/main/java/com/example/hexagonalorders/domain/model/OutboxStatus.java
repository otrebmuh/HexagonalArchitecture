package com.example.hexagonalorders.domain.model;

/**
 * Represents the status of an outbox message.
 */
public enum OutboxStatus {
    PENDING,
    PROCESSED,
    FAILED
} 