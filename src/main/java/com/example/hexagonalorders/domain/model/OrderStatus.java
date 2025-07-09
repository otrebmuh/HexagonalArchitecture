package com.example.hexagonalorders.domain.model;

/**
 * Represents the current status of an order in the system.
 * This enum defines the possible states an order can be in throughout its lifecycle.
 */
public enum OrderStatus {
    /** Order has been created but not yet confirmed by the system or customer */
    PENDING,
    
    /** Order has been validated and confirmed, ready for processing */
    CONFIRMED,
    
    /** Order has been processed and shipped to the customer */
    SHIPPED,
    
    /** Order has been successfully delivered to the customer */
    DELIVERED,
    
    /** Order has been cancelled and is no longer active */
    CANCELLED
} 