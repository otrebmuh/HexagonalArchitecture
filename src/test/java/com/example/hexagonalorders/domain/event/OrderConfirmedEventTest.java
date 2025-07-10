package com.example.hexagonalorders.domain.event;

import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderConfirmedEventTest {

    @Test
    void shouldCreateOrderConfirmedEvent() {
        // Given
        Long orderId = 1L;
        OrderNumber orderNumber = new OrderNumber("ORD-001");

        // When
        OrderConfirmedEvent event = new OrderConfirmedEvent(orderId, orderNumber);

        // Then
        assertNotNull(event);
        assertEquals(orderId, event.getOrderId());
        assertEquals(orderNumber, event.getOrderNumber());
        assertNotNull(event.getEventId());
        assertNotNull(event.getOccurredOn());
    }

    @Test
    void shouldHaveDifferentEventIdsForDifferentEvents() {
        // Given
        Long orderId = 1L;
        OrderNumber orderNumber = new OrderNumber("ORD-001");

        // When
        OrderConfirmedEvent event1 = new OrderConfirmedEvent(orderId, orderNumber);
        OrderConfirmedEvent event2 = new OrderConfirmedEvent(orderId, orderNumber);

        // Then
        assertNotEquals(event1.getEventId(), event2.getEventId());
    }
} 