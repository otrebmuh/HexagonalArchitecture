package com.example.hexagonalorders.domain.model;

import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import java.math.BigDecimal;

/**
 * Core domain entity representing an OrderItem in the system.
 * This class is part of the domain layer and represents a line item within an Order.
 * It is independent of any infrastructure concerns.
 * 
 * An OrderItem consists of:
 * - A unique identifier
 * - A product number (as a value object)
 * - A quantity (as a value object)
 */
public record OrderItem(
    ProductNumber productNumber,
    Quantity quantity,
    BigDecimal unitPrice
) {
    public OrderItem {
        if (productNumber == null) {
            throw new IllegalArgumentException("Product number cannot be null");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than zero");
        }
    }
} 