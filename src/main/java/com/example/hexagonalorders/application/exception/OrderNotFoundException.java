package com.example.hexagonalorders.application.exception;

import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;

/**
 * Exception thrown when an order is not found.
 */
public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(OrderNumber orderNumber) {
        super("Order not found with number: " + orderNumber.value());
    }
    
    public OrderNotFoundException(String message) {
        super(message);
    }
} 