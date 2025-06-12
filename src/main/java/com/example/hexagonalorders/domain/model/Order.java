package com.example.hexagonalorders.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Core domain entity representing an Order in the system.
 * This class is part of the domain layer and contains the business logic and rules
 * related to orders. It is independent of any infrastructure concerns.
 * 
 * An Order consists of:
 * - A unique identifier
 * - An order number
 * - A creation date
 * - A list of order items
 */
@Getter
@Setter
public class Order {
    private Long id;
    private String orderNumber;
    private LocalDateTime creationDate;
    private List<OrderItem> items;

    public Order() {
        this.items = new ArrayList<>();
        this.creationDate = LocalDateTime.now();
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
    }
} 