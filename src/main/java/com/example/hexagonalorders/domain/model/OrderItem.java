package com.example.hexagonalorders.domain.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Core domain entity representing an OrderItem in the system.
 * This class is part of the domain layer and represents a line item within an Order.
 * It is independent of any infrastructure concerns.
 * 
 * An OrderItem consists of:
 * - A unique identifier
 * - A product number
 * - A quantity
 */
@Getter
@Setter
public class OrderItem {
    private Long id;
    private String productNumber;
    private Integer quantity;
} 