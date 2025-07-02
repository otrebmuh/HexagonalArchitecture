package com.example.hexagonalorders.domain.service;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OrderItem;
import com.example.hexagonalorders.domain.model.OrderStatus;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderValidationServiceTest {

    private OrderValidationService validationService;
    private Order validOrder;

    @BeforeEach
    void setUp() {
        validationService = new OrderValidationService();
        
        // Create a valid order for testing
        OrderNumber orderNumber = new OrderNumber("ORD-001");
        String customerId = "CUST-001";
        LocalDateTime orderDate = LocalDateTime.now();
        OrderStatus status = OrderStatus.PENDING;
        
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(
            new ProductNumber("PROD-001"),
            new Quantity(2),
            new BigDecimal("29.99")
        ));
        
        validOrder = new Order(orderNumber, customerId, orderDate, items, status);
    }

    @Test
    void shouldValidateOrderWithValidParameters() {
        // When & Then
        assertDoesNotThrow(() -> validationService.validateOrder(validOrder));
    }

    @Test
    void shouldThrowExceptionWhenOrderIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> validationService.validateOrder(null)
        );
        assertEquals("Order cannot be null", exception.getMessage());
    }

    @Test
    void shouldValidateOrderWithoutOrderNumber() {
        // Given
        String customerId = "CUST-001";
        LocalDateTime orderDate = LocalDateTime.now();
        OrderStatus status = OrderStatus.PENDING;
        
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(
            new ProductNumber("PROD-001"),
            new Quantity(2),
            new BigDecimal("29.99")
        ));
        
        Order orderWithoutNumber = new Order(customerId, orderDate, items, status);

        // When & Then
        assertDoesNotThrow(() -> validationService.validateOrder(orderWithoutNumber));
    }

    @Test
    void shouldValidateOrderWithMultipleItems() {
        // Given
        OrderNumber orderNumber = new OrderNumber("ORD-002");
        String customerId = "CUST-002";
        LocalDateTime orderDate = LocalDateTime.now();
        OrderStatus status = OrderStatus.PENDING;
        
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(
            new ProductNumber("PROD-001"),
            new Quantity(2),
            new BigDecimal("29.99")
        ));
        items.add(new OrderItem(
            new ProductNumber("PROD-002"),
            new Quantity(1),
            new BigDecimal("19.99")
        ));
        
        Order orderWithMultipleItems = new Order(orderNumber, customerId, orderDate, items, status);

        // When & Then
        assertDoesNotThrow(() -> validationService.validateOrder(orderWithMultipleItems));
    }

    @Test
    void shouldValidateOrderWithDifferentStatuses() {
        // Given
        OrderNumber orderNumber = new OrderNumber("ORD-003");
        String customerId = "CUST-003";
        LocalDateTime orderDate = LocalDateTime.now();
        
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(
            new ProductNumber("PROD-001"),
            new Quantity(1),
            new BigDecimal("29.99")
        ));

        // Test all possible statuses
        for (OrderStatus status : OrderStatus.values()) {
            Order orderWithStatus = new Order(orderNumber, customerId, orderDate, items, status);
            
            // When & Then
            assertDoesNotThrow(() -> validationService.validateOrder(orderWithStatus));
        }
    }

    @Test
    void shouldValidateOrderWithEmptyItemsList() {
        // Given
        OrderNumber orderNumber = new OrderNumber("ORD-004");
        String customerId = "CUST-004";
        LocalDateTime orderDate = LocalDateTime.now();
        OrderStatus status = OrderStatus.PENDING;
        
        List<OrderItem> emptyItems = new ArrayList<>();
        
        Order orderWithEmptyItems = new Order(orderNumber, customerId, orderDate, emptyItems, status);

        // When & Then
        assertDoesNotThrow(() -> validationService.validateOrder(orderWithEmptyItems));
    }

    @Test
    void shouldValidateOrderWithComplexCustomerId() {
        // Given
        OrderNumber orderNumber = new OrderNumber("ORD-005");
        String complexCustomerId = "CUSTOMER-2024-001-ABC123";
        LocalDateTime orderDate = LocalDateTime.now();
        OrderStatus status = OrderStatus.PENDING;
        
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(
            new ProductNumber("PROD-001"),
            new Quantity(1),
            new BigDecimal("29.99")
        ));
        
        Order orderWithComplexCustomerId = new Order(orderNumber, complexCustomerId, orderDate, items, status);

        // When & Then
        assertDoesNotThrow(() -> validationService.validateOrder(orderWithComplexCustomerId));
    }

    @Test
    void shouldValidateOrderWithHighValueItems() {
        // Given
        OrderNumber orderNumber = new OrderNumber("ORD-006");
        String customerId = "CUST-006";
        LocalDateTime orderDate = LocalDateTime.now();
        OrderStatus status = OrderStatus.PENDING;
        
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(
            new ProductNumber("PROD-001"),
            new Quantity(1),
            new BigDecimal("999999.99")
        ));
        
        Order orderWithHighValueItem = new Order(orderNumber, customerId, orderDate, items, status);

        // When & Then
        assertDoesNotThrow(() -> validationService.validateOrder(orderWithHighValueItem));
    }

    @Test
    void shouldValidateOrderWithLowValueItems() {
        // Given
        OrderNumber orderNumber = new OrderNumber("ORD-007");
        String customerId = "CUST-007";
        LocalDateTime orderDate = LocalDateTime.now();
        OrderStatus status = OrderStatus.PENDING;
        
        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(
            new ProductNumber("PROD-001"),
            new Quantity(1),
            new BigDecimal("0.01")
        ));
        
        Order orderWithLowValueItem = new Order(orderNumber, customerId, orderDate, items, status);

        // When & Then
        assertDoesNotThrow(() -> validationService.validateOrder(orderWithLowValueItem));
    }
} 