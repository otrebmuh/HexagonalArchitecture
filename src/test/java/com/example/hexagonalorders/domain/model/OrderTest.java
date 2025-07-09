package com.example.hexagonalorders.domain.model;

import com.example.hexagonalorders.domain.event.DomainEvent;
import com.example.hexagonalorders.domain.event.OrderCreatedEvent;
import com.example.hexagonalorders.domain.event.OrderItemAddedEvent;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import com.example.hexagonalorders.domain.model.valueobject.ShippingAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private OrderNumber validOrderNumber;
    private String validCustomerId;
    private LocalDateTime validOrderDate;
    private List<OrderItem> validItems;
    private OrderStatus validStatus;
    private ShippingAddress validShippingAddress;

    @BeforeEach
    void setUp() {
        validOrderNumber = new OrderNumber("ORD-001");
        validCustomerId = "CUST-001";
        validOrderDate = LocalDateTime.now();
        validStatus = OrderStatus.PENDING;
        
        validItems = new ArrayList<>();
        validItems.add(new OrderItem(
            new ProductNumber("PROD-001"),
            new Quantity(2),
            new BigDecimal("29.99")
        ));
        validShippingAddress = new ShippingAddress("123 Main St", "City", "State", "12345", "Country");
    }

    @Test
    void shouldCreateOrderWithValidParameters() {
        // When
        Order order = new Order(validOrderNumber, validCustomerId, validOrderDate, validItems, validShippingAddress, validStatus);

        // Then
        assertNotNull(order);
        assertEquals(validOrderNumber, order.getOrderNumber());
        assertEquals(validCustomerId, order.getCustomerId());
        assertEquals(validOrderDate, order.getOrderDate());
        assertEquals(validItems, order.getItems());
        assertEquals(validStatus, order.getStatus());
        assertEquals(validShippingAddress, order.getShippingAddress());
        assertFalse(order.getDomainEvents().isEmpty());
        assertTrue(order.getDomainEvents().get(0) instanceof OrderCreatedEvent);
    }

    @Test
    void shouldCreateOrderWithoutOrderNumber() {
        // When
        Order order = new Order(validCustomerId, validOrderDate, validItems, validShippingAddress, validStatus);

        // Then
        assertNotNull(order);
        assertNull(order.getOrderNumber());
        assertEquals(validCustomerId, order.getCustomerId());
        assertEquals(validOrderDate, order.getOrderDate());
        assertEquals(validItems, order.getItems());
        assertEquals(validStatus, order.getStatus());
        assertEquals(validShippingAddress, order.getShippingAddress());
        assertTrue(order.getDomainEvents().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenOrderNumberIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Order(null, validCustomerId, validOrderDate, validItems, validShippingAddress, validStatus)
        );
        assertEquals("Order number cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCustomerIdIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Order(validOrderNumber, null, validOrderDate, validItems, validShippingAddress, validStatus)
        );
        assertEquals("Customer ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCustomerIdIsEmpty() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Order(validOrderNumber, "", validOrderDate, validItems, validShippingAddress, validStatus)
        );
        assertEquals("Customer ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCustomerIdIsBlank() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Order(validOrderNumber, "   ", validOrderDate, validItems, validShippingAddress, validStatus)
        );
        assertEquals("Customer ID cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOrderDateIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Order(validOrderNumber, validCustomerId, null, validItems, validShippingAddress, validStatus)
        );
        assertEquals("Order date cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenItemsIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Order(validOrderNumber, validCustomerId, validOrderDate, null, validShippingAddress, validStatus)
        );
        assertEquals("Items cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenStatusIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Order(validOrderNumber, validCustomerId, validOrderDate, validItems, validShippingAddress, null)
        );
        assertEquals("Status cannot be null", exception.getMessage());
    }

    @Test
    void shouldAddItemToOrder() {
        // Given
        Order order = new Order(validOrderNumber, validCustomerId, validOrderDate, validItems, validShippingAddress, validStatus);
        OrderItem newItem = new OrderItem(
            new ProductNumber("PROD-002"),
            new Quantity(1),
            new BigDecimal("19.99")
        );
        int initialEventCount = order.getDomainEvents().size();

        // When
        order.addItem(newItem, 1L, 2L);

        // Then
        assertTrue(order.getItems().contains(newItem));
        assertEquals(initialEventCount + 1, order.getDomainEvents().size());
        
        DomainEvent lastEvent = order.getDomainEvents().get(order.getDomainEvents().size() - 1);
        assertTrue(lastEvent instanceof OrderItemAddedEvent);
        
        OrderItemAddedEvent itemAddedEvent = (OrderItemAddedEvent) lastEvent;
        assertEquals(1L, itemAddedEvent.getOrderId());
        assertEquals(2L, itemAddedEvent.getItemId());
        assertEquals(newItem.getProductNumber(), itemAddedEvent.getProductNumber());
        assertEquals(newItem.getQuantity(), itemAddedEvent.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenAddingNullItem() {
        // Given
        Order order = new Order(validOrderNumber, validCustomerId, validOrderDate, validItems, validShippingAddress, validStatus);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> order.addItem(null, 1L, 2L)
        );
        assertEquals("Order item cannot be null", exception.getMessage());
    }

    @Test
    void shouldRemoveItemFromOrder() {
        // Given
        Order order = new Order(validOrderNumber, validCustomerId, validOrderDate, validItems, validShippingAddress, validStatus);
        OrderItem itemToRemove = validItems.get(0);

        // When
        order.removeItem(itemToRemove);

        // Then
        assertFalse(order.getItems().contains(itemToRemove));
    }

    @Test
    void shouldThrowExceptionWhenRemovingNullItem() {
        // Given
        Order order = new Order(validOrderNumber, validCustomerId, validOrderDate, validItems, validShippingAddress, validStatus);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> order.removeItem(null)
        );
        assertEquals("Order item cannot be null", exception.getMessage());
    }

    @Test
    void shouldClearDomainEvents() {
        // Given
        Order order = new Order(validOrderNumber, validCustomerId, validOrderDate, validItems, validShippingAddress, validStatus);
        assertFalse(order.getDomainEvents().isEmpty());

        // When
        order.clearDomainEvents();

        // Then
        assertTrue(order.getDomainEvents().isEmpty());
    }

    @Test
    void shouldReturnUnmodifiableListOfDomainEvents() {
        // Given
        Order order = new Order(validOrderNumber, validCustomerId, validOrderDate, validItems, validShippingAddress, validStatus);

        // When & Then
        assertThrows(UnsupportedOperationException.class, () -> {
            order.getDomainEvents().add(new OrderCreatedEvent(1L, validOrderNumber, validShippingAddress));
        });
    }
} 