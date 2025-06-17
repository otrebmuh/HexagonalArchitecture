package com.example.hexagonalorders.domain.model;

import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import lombok.Getter;
import lombok.Setter;

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
@Getter
@Setter
public class OrderItem {
    private Long id;
    private ProductNumber productNumber;
    private Quantity quantity;
} 