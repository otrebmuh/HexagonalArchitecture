package com.example.hexagonalorders.application.port.out;

/**
 * Output port defining the contract for order number generation.
 * This interface is part of the application layer and defines how the application
 * expects to generate unique order numbers.
 * 
 * Implementations of this port should be provided by the adapter layer
 * (e.g., using UUID, sequence numbers, etc.).
 */
public interface OrderNumberGenerator {
    String generateOrderNumber();
} 