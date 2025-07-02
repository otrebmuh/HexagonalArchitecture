package com.example.hexagonalorders.domain.event;

import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderCreatedEventTest {

    @Test
    void shouldCreateOrderCreatedEventWithValidParameters() {
        // Given
        Long orderId = 1L;
        OrderNumber orderNumber = new OrderNumber("ORD-001");

        // When
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, orderNumber);

        // Then
        assertNotNull(event);
        assertEquals(orderId, event.getOrderId());
        assertEquals(orderNumber, event.getOrderNumber());
    }

    @Test
    void shouldCreateOrderCreatedEventWithNullOrderId() {
        // Given
        OrderNumber orderNumber = new OrderNumber("ORD-001");

        // When
        OrderCreatedEvent event = new OrderCreatedEvent(null, orderNumber);

        // Then
        assertNotNull(event);
        assertNull(event.getOrderId());
        assertEquals(orderNumber, event.getOrderNumber());
    }

    @Test
    void shouldCreateOrderCreatedEventWithNullOrderNumber() {
        // Given
        Long orderId = 1L;

        // When
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, null);

        // Then
        assertNotNull(event);
        assertEquals(orderId, event.getOrderId());
        assertNull(event.getOrderNumber());
    }

    @Test
    void shouldCreateOrderCreatedEventWithBothNullParameters() {
        // When
        OrderCreatedEvent event = new OrderCreatedEvent(null, null);

        // Then
        assertNotNull(event);
        assertNull(event.getOrderId());
        assertNull(event.getOrderNumber());
    }

    @Test
    void shouldHaveSameContentWhenParametersAreEqual() {
        // Given
        Long orderId = 1L;
        OrderNumber orderNumber = new OrderNumber("ORD-001");
        OrderCreatedEvent event1 = new OrderCreatedEvent(orderId, orderNumber);
        OrderCreatedEvent event2 = new OrderCreatedEvent(orderId, orderNumber);

        // When & Then
        assertEquals(event1.getOrderId(), event2.getOrderId());
        assertEquals(event1.getOrderNumber(), event2.getOrderNumber());
    }

    @Test
    void shouldHaveDifferentContentWhenOrderIdsAreDifferent() {
        // Given
        OrderNumber orderNumber = new OrderNumber("ORD-001");
        OrderCreatedEvent event1 = new OrderCreatedEvent(1L, orderNumber);
        OrderCreatedEvent event2 = new OrderCreatedEvent(2L, orderNumber);

        // When & Then
        assertNotEquals(event1.getOrderId(), event2.getOrderId());
    }

    @Test
    void shouldHaveDifferentContentWhenOrderNumbersAreDifferent() {
        // Given
        Long orderId = 1L;
        OrderCreatedEvent event1 = new OrderCreatedEvent(orderId, new OrderNumber("ORD-001"));
        OrderCreatedEvent event2 = new OrderCreatedEvent(orderId, new OrderNumber("ORD-002"));

        // When & Then
        assertNotEquals(event1.getOrderNumber(), event2.getOrderNumber());
    }

    @Test
    void shouldExtendDomainEvent() {
        // Given
        Long orderId = 1L;
        OrderNumber orderNumber = new OrderNumber("ORD-001");

        // When
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, orderNumber);

        // Then
        assertTrue(event instanceof DomainEvent);
    }
} 