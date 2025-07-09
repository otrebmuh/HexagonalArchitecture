package com.example.hexagonalorders.domain.model;

import com.example.hexagonalorders.domain.event.DomainEvent;
import com.example.hexagonalorders.domain.event.OrderCreatedEvent;
import com.example.hexagonalorders.domain.event.OrderItemAddedEvent;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ShippingAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Core domain entity representing an Order in the system.
 * This class is part of the domain layer and contains the business logic and rules
 * related to orders. It is independent of any infrastructure concerns.
 * 
 * An Order consists of:
 * - A unique identifier
 * - An order number (as a value object)
 * - A customer ID (reference to Customer aggregate)
 * - A creation date
 * - A list of order items
 * - A list of domain events
 */
public class Order {
    private final OrderNumber orderNumber;
    private final String customerId;
    private final LocalDateTime orderDate;
    private final List<OrderItem> items;
    private final ShippingAddress shippingAddress;
    private final OrderStatus status;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Order(OrderNumber orderNumber, String customerId, LocalDateTime orderDate, List<OrderItem> items, ShippingAddress shippingAddress, OrderStatus status) {
        if (orderNumber == null) {
            throw new IllegalArgumentException("Order number cannot be null");
        }
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
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
        if (shippingAddress == null) {
            throw new IllegalArgumentException("Shipping address cannot be null");
        }
        this.orderNumber = orderNumber;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.status = status;
        domainEvents.add(new OrderCreatedEvent(null, orderNumber, shippingAddress));
    }

    public Order(String customerId, LocalDateTime orderDate, List<OrderItem> items, ShippingAddress shippingAddress, OrderStatus status) {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer ID cannot be null or empty");
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
        if (shippingAddress == null) {
            throw new IllegalArgumentException("Shipping address cannot be null");
        }
        this.orderNumber = null;
        this.customerId = customerId;
        this.orderDate = orderDate;
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.status = status;
        // No domain event here
    }

    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    /**
     * Adds an item to the order. Business rules (e.g., no duplicates, min/max quantity, etc.)
     * should be enforced here.
     * Raises an OrderItemAddedEvent.
     */
    public void addItem(OrderItem item, Long orderId, Long itemId) {
        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }
        // TODO: Enforce business rules here (e.g., no duplicate products, min/max quantity, etc.)
        this.items.add(item);
        domainEvents.add(new OrderItemAddedEvent(orderId, itemId, item.getProductNumber(), item.getQuantity()));
    }

    /**
     * Removes an item from the order. Business rules (e.g., order must have at least one item)
     * should be enforced here.
     */
    public void removeItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Order item cannot be null");
        }
        // TODO: Enforce business rules here (e.g., order must have at least one item)
        this.items.remove(item);
    }

    // equals, hashCode, and toString can be added as needed
} 