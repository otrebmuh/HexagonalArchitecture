package com.example.hexagonalorders.domain.event;

import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemAddedEventTest {

    @Test
    void shouldCreateOrderItemAddedEventWithValidParameters() {
        // Given
        Long orderId = 1L;
        Long itemId = 2L;
        ProductNumber productNumber = new ProductNumber("PROD-001");
        Quantity quantity = new Quantity(3);

        // When
        OrderItemAddedEvent event = new OrderItemAddedEvent(orderId, itemId, productNumber, quantity);

        // Then
        assertNotNull(event);
        assertEquals(orderId, event.getOrderId());
        assertEquals(itemId, event.getItemId());
        assertEquals(productNumber, event.getProductNumber());
        assertEquals(quantity, event.getQuantity());
    }

    @Test
    void shouldCreateOrderItemAddedEventWithNullOrderId() {
        // Given
        Long itemId = 2L;
        ProductNumber productNumber = new ProductNumber("PROD-001");
        Quantity quantity = new Quantity(3);

        // When
        OrderItemAddedEvent event = new OrderItemAddedEvent(null, itemId, productNumber, quantity);

        // Then
        assertNotNull(event);
        assertNull(event.getOrderId());
        assertEquals(itemId, event.getItemId());
        assertEquals(productNumber, event.getProductNumber());
        assertEquals(quantity, event.getQuantity());
    }

    @Test
    void shouldCreateOrderItemAddedEventWithNullItemId() {
        // Given
        Long orderId = 1L;
        ProductNumber productNumber = new ProductNumber("PROD-001");
        Quantity quantity = new Quantity(3);

        // When
        OrderItemAddedEvent event = new OrderItemAddedEvent(orderId, null, productNumber, quantity);

        // Then
        assertNotNull(event);
        assertEquals(orderId, event.getOrderId());
        assertNull(event.getItemId());
        assertEquals(productNumber, event.getProductNumber());
        assertEquals(quantity, event.getQuantity());
    }

    @Test
    void shouldCreateOrderItemAddedEventWithNullProductNumber() {
        // Given
        Long orderId = 1L;
        Long itemId = 2L;
        Quantity quantity = new Quantity(3);

        // When
        OrderItemAddedEvent event = new OrderItemAddedEvent(orderId, itemId, null, quantity);

        // Then
        assertNotNull(event);
        assertEquals(orderId, event.getOrderId());
        assertEquals(itemId, event.getItemId());
        assertNull(event.getProductNumber());
        assertEquals(quantity, event.getQuantity());
    }

    @Test
    void shouldCreateOrderItemAddedEventWithNullQuantity() {
        // Given
        Long orderId = 1L;
        Long itemId = 2L;
        ProductNumber productNumber = new ProductNumber("PROD-001");

        // When
        OrderItemAddedEvent event = new OrderItemAddedEvent(orderId, itemId, productNumber, null);

        // Then
        assertNotNull(event);
        assertEquals(orderId, event.getOrderId());
        assertEquals(itemId, event.getItemId());
        assertEquals(productNumber, event.getProductNumber());
        assertNull(event.getQuantity());
    }

    @Test
    void shouldCreateOrderItemAddedEventWithAllNullParameters() {
        // When
        OrderItemAddedEvent event = new OrderItemAddedEvent(null, null, null, null);

        // Then
        assertNotNull(event);
        assertNull(event.getOrderId());
        assertNull(event.getItemId());
        assertNull(event.getProductNumber());
        assertNull(event.getQuantity());
    }

    @Test
    void shouldHaveSameContentWhenParametersAreEqual() {
        // Given
        Long orderId = 1L;
        Long itemId = 2L;
        ProductNumber productNumber = new ProductNumber("PROD-001");
        Quantity quantity = new Quantity(3);
        OrderItemAddedEvent event1 = new OrderItemAddedEvent(orderId, itemId, productNumber, quantity);
        OrderItemAddedEvent event2 = new OrderItemAddedEvent(orderId, itemId, productNumber, quantity);

        // When & Then
        assertEquals(event1.getOrderId(), event2.getOrderId());
        assertEquals(event1.getItemId(), event2.getItemId());
        assertEquals(event1.getProductNumber(), event2.getProductNumber());
        assertEquals(event1.getQuantity(), event2.getQuantity());
    }

    @Test
    void shouldHaveDifferentContentWhenOrderIdsAreDifferent() {
        // Given
        Long itemId = 2L;
        ProductNumber productNumber = new ProductNumber("PROD-001");
        Quantity quantity = new Quantity(3);
        OrderItemAddedEvent event1 = new OrderItemAddedEvent(1L, itemId, productNumber, quantity);
        OrderItemAddedEvent event2 = new OrderItemAddedEvent(2L, itemId, productNumber, quantity);

        // When & Then
        assertNotEquals(event1.getOrderId(), event2.getOrderId());
    }

    @Test
    void shouldHaveDifferentContentWhenItemIdsAreDifferent() {
        // Given
        Long orderId = 1L;
        ProductNumber productNumber = new ProductNumber("PROD-001");
        Quantity quantity = new Quantity(3);
        OrderItemAddedEvent event1 = new OrderItemAddedEvent(orderId, 1L, productNumber, quantity);
        OrderItemAddedEvent event2 = new OrderItemAddedEvent(orderId, 2L, productNumber, quantity);

        // When & Then
        assertNotEquals(event1.getItemId(), event2.getItemId());
    }

    @Test
    void shouldHaveDifferentContentWhenProductNumbersAreDifferent() {
        // Given
        Long orderId = 1L;
        Long itemId = 2L;
        Quantity quantity = new Quantity(3);
        OrderItemAddedEvent event1 = new OrderItemAddedEvent(orderId, itemId, new ProductNumber("PROD-001"), quantity);
        OrderItemAddedEvent event2 = new OrderItemAddedEvent(orderId, itemId, new ProductNumber("PROD-002"), quantity);

        // When & Then
        assertNotEquals(event1.getProductNumber(), event2.getProductNumber());
    }

    @Test
    void shouldHaveDifferentContentWhenQuantitiesAreDifferent() {
        // Given
        Long orderId = 1L;
        Long itemId = 2L;
        ProductNumber productNumber = new ProductNumber("PROD-001");
        OrderItemAddedEvent event1 = new OrderItemAddedEvent(orderId, itemId, productNumber, new Quantity(3));
        OrderItemAddedEvent event2 = new OrderItemAddedEvent(orderId, itemId, productNumber, new Quantity(5));

        // When & Then
        assertNotEquals(event1.getQuantity(), event2.getQuantity());
    }

    @Test
    void shouldExtendDomainEvent() {
        // Given
        Long orderId = 1L;
        Long itemId = 2L;
        ProductNumber productNumber = new ProductNumber("PROD-001");
        Quantity quantity = new Quantity(3);

        // When
        OrderItemAddedEvent event = new OrderItemAddedEvent(orderId, itemId, productNumber, quantity);

        // Then
        assertTrue(event instanceof DomainEvent);
    }
} 