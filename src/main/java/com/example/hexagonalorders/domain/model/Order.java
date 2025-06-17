package com.example.hexagonalorders.domain.model;

import com.example.hexagonalorders.domain.event.DomainEvent;
import com.example.hexagonalorders.domain.event.OrderCreatedEvent;
import com.example.hexagonalorders.domain.event.OrderItemAddedEvent;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
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
 * - An order number (as a value object)
 * - A creation date
 * - A list of order items
 * - A list of domain events
 */
@Getter
@Setter
public class Order {
    private Long id;
    private OrderNumber orderNumber;
    private LocalDateTime creationDate;
    private List<OrderItem> items;
    private final List<DomainEvent> domainEvents;

    public Order() {
        this.items = new ArrayList<>();
        this.domainEvents = new ArrayList<>();
        this.creationDate = LocalDateTime.now();
    }

    public void addItem(OrderItem item) {
        this.items.add(item);
        this.domainEvents.add(new OrderItemAddedEvent(
            this.id,
            item.getId(),
            item.getProductNumber(),
            item.getQuantity()
        ));
    }

    public void setOrderNumber(OrderNumber orderNumber) {
        this.orderNumber = orderNumber;
        if (this.id != null) {
            this.domainEvents.add(new OrderCreatedEvent(this.id, orderNumber));
        }
    }

    public List<DomainEvent> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
} 