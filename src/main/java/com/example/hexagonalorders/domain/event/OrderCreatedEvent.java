package com.example.hexagonalorders.domain.event;

import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ShippingAddress;

/**
 * Event raised when a new order is created.
 */
public class OrderCreatedEvent extends DomainEvent {
    private final Long orderId;
    private final OrderNumber orderNumber;
    private final ShippingAddress shippingAddress;

    public OrderCreatedEvent(Long orderId, OrderNumber orderNumber, ShippingAddress shippingAddress) {
        super();
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.shippingAddress = shippingAddress;
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderNumber getOrderNumber() {
        return orderNumber;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }
} 