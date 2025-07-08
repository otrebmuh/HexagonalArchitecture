package com.example.hexagonalorders.domain.event;

import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ShippingAddress;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderCreatedEventTest {

    @Test
    void shouldCreateOrderCreatedEventWithValidParameters() {
        // Given
        Long orderId = 1L;
        OrderNumber orderNumber = new OrderNumber("ORD-001");
        ShippingAddress shippingAddress = new ShippingAddress("123 Main St", "City", "State", "12345", "Country");
        // When
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, orderNumber, shippingAddress);
        // Then
        assertNotNull(event);
        assertEquals(orderId, event.getOrderId());
        assertEquals(orderNumber, event.getOrderNumber());
        assertEquals(shippingAddress, event.getShippingAddress());
    }

    @Test
    void shouldCreateOrderCreatedEventWithNullOrderId() {
        // Given
        OrderNumber orderNumber = new OrderNumber("ORD-001");
        ShippingAddress shippingAddress = new ShippingAddress("123 Main St", "City", "State", "12345", "Country");
        // When
        OrderCreatedEvent event = new OrderCreatedEvent(null, orderNumber, shippingAddress);
        // Then
        assertNotNull(event);
        assertNull(event.getOrderId());
        assertEquals(orderNumber, event.getOrderNumber());
        assertEquals(shippingAddress, event.getShippingAddress());
    }

    @Test
    void shouldCreateOrderCreatedEventWithNullOrderNumber() {
        // Given
        Long orderId = 1L;
        ShippingAddress shippingAddress = new ShippingAddress("123 Main St", "City", "State", "12345", "Country");
        // When
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, null, shippingAddress);
        // Then
        assertNotNull(event);
        assertEquals(orderId, event.getOrderId());
        assertNull(event.getOrderNumber());
        assertEquals(shippingAddress, event.getShippingAddress());
    }

    @Test
    void shouldCreateOrderCreatedEventWithBothNullParameters() {
        // When
        ShippingAddress shippingAddress = new ShippingAddress("123 Main St", "City", "State", "12345", "Country");
        OrderCreatedEvent event = new OrderCreatedEvent(null, null, shippingAddress);
        // Then
        assertNotNull(event);
        assertNull(event.getOrderId());
        assertNull(event.getOrderNumber());
        assertEquals(shippingAddress, event.getShippingAddress());
    }

    @Test
    void shouldHaveSameContentWhenParametersAreEqual() {
        // Given
        Long orderId = 1L;
        OrderNumber orderNumber = new OrderNumber("ORD-001");
        ShippingAddress shippingAddress = new ShippingAddress("123 Main St", "City", "State", "12345", "Country");
        OrderCreatedEvent event1 = new OrderCreatedEvent(orderId, orderNumber, shippingAddress);
        OrderCreatedEvent event2 = new OrderCreatedEvent(orderId, orderNumber, shippingAddress);
        // When & Then
        assertEquals(event1.getOrderId(), event2.getOrderId());
        assertEquals(event1.getOrderNumber(), event2.getOrderNumber());
        assertEquals(event1.getShippingAddress(), event2.getShippingAddress());
    }

    @Test
    void shouldHaveDifferentContentWhenOrderIdsAreDifferent() {
        // Given
        OrderNumber orderNumber = new OrderNumber("ORD-001");
        ShippingAddress shippingAddress = new ShippingAddress("123 Main St", "City", "State", "12345", "Country");
        OrderCreatedEvent event1 = new OrderCreatedEvent(1L, orderNumber, shippingAddress);
        OrderCreatedEvent event2 = new OrderCreatedEvent(2L, orderNumber, shippingAddress);
        // When & Then
        assertNotEquals(event1.getOrderId(), event2.getOrderId());
    }

    @Test
    void shouldHaveDifferentContentWhenOrderNumbersAreDifferent() {
        // Given
        Long orderId = 1L;
        ShippingAddress shippingAddress = new ShippingAddress("123 Main St", "City", "State", "12345", "Country");
        OrderCreatedEvent event1 = new OrderCreatedEvent(orderId, new OrderNumber("ORD-001"), shippingAddress);
        OrderCreatedEvent event2 = new OrderCreatedEvent(orderId, new OrderNumber("ORD-002"), shippingAddress);
        // When & Then
        assertNotEquals(event1.getOrderNumber(), event2.getOrderNumber());
    }

    @Test
    void shouldExtendDomainEvent() {
        // Given
        Long orderId = 1L;
        OrderNumber orderNumber = new OrderNumber("ORD-001");
        ShippingAddress shippingAddress = new ShippingAddress("123 Main St", "City", "State", "12345", "Country");
        // When
        OrderCreatedEvent event = new OrderCreatedEvent(orderId, orderNumber, shippingAddress);
        // Then
        assertTrue(event instanceof DomainEvent);
    }
} 