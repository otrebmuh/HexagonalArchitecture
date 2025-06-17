package com.example.hexagonalorders.domain.model;

import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Core domain entity representing an Order in the system.
 * This class is part of the domain layer and contains the business logic and rules
 * related to orders. It is independent of any infrastructure concerns.
 * 
 * An Order consists of:
 * - A unique identifier
 * - An order number (as a value object)
 * - A creation date
 * - A list of order items
 * - A list of domain events
 */
public record Order(
    OrderNumber orderNumber,
    String customerName,
    LocalDateTime orderDate,
    List<OrderItem> items,
    OrderStatus status
) {
    public Order {
        if (orderNumber == null) {
            throw new IllegalArgumentException("Order number cannot be null");
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        if (orderDate == null) {
            throw new IllegalArgumentException("Order date cannot be null");
        }
        if (items == null) {
            throw new IllegalArgumentException("Items cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
    }
} 