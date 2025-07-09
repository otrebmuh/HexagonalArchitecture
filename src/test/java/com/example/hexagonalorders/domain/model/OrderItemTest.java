package com.example.hexagonalorders.domain.model;

import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void shouldCreateOrderItemWithValidParameters() {
        // Given
        ProductNumber productNumber = new ProductNumber("PROD-001");
        Quantity quantity = new Quantity(2);
        BigDecimal unitPrice = new BigDecimal("29.99");

        // When
        OrderItem orderItem = new OrderItem(productNumber, quantity, unitPrice);

        // Then
        assertNotNull(orderItem);
        assertEquals(productNumber, orderItem.getProductNumber());
        assertEquals(quantity, orderItem.getQuantity());
        assertEquals(unitPrice, orderItem.getUnitPrice());
    }

    @Test
    void shouldThrowExceptionWhenProductNumberIsNull() {
        // Given
        Quantity quantity = new Quantity(2);
        BigDecimal unitPrice = new BigDecimal("29.99");

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new OrderItem(null, quantity, unitPrice)
        );
        assertEquals("Product number cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenQuantityIsNull() {
        // Given
        ProductNumber productNumber = new ProductNumber("PROD-001");
        BigDecimal unitPrice = new BigDecimal("29.99");

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new OrderItem(productNumber, null, unitPrice)
        );
        assertEquals("Quantity cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUnitPriceIsNull() {
        // Given
        ProductNumber productNumber = new ProductNumber("PROD-001");
        Quantity quantity = new Quantity(2);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new OrderItem(productNumber, quantity, null)
        );
        assertEquals("Unit price must be greater than zero", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUnitPriceIsZero() {
        // Given
        ProductNumber productNumber = new ProductNumber("PROD-001");
        Quantity quantity = new Quantity(2);
        BigDecimal unitPrice = BigDecimal.ZERO;

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new OrderItem(productNumber, quantity, unitPrice)
        );
        assertEquals("Unit price must be greater than zero", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUnitPriceIsNegative() {
        // Given
        ProductNumber productNumber = new ProductNumber("PROD-001");
        Quantity quantity = new Quantity(2);
        BigDecimal unitPrice = new BigDecimal("-10.00");

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new OrderItem(productNumber, quantity, unitPrice)
        );
        assertEquals("Unit price must be greater than zero", exception.getMessage());
    }

    @Test
    void shouldCreateOrderItemWithHighUnitPrice() {
        // Given
        ProductNumber productNumber = new ProductNumber("PROD-001");
        Quantity quantity = new Quantity(1);
        BigDecimal unitPrice = new BigDecimal("999999.99");

        // When
        OrderItem orderItem = new OrderItem(productNumber, quantity, unitPrice);

        // Then
        assertNotNull(orderItem);
        assertEquals(unitPrice, orderItem.getUnitPrice());
    }

    @Test
    void shouldCreateOrderItemWithDecimalUnitPrice() {
        // Given
        ProductNumber productNumber = new ProductNumber("PROD-001");
        Quantity quantity = new Quantity(1);
        BigDecimal unitPrice = new BigDecimal("0.01");

        // When
        OrderItem orderItem = new OrderItem(productNumber, quantity, unitPrice);

        // Then
        assertNotNull(orderItem);
        assertEquals(unitPrice, orderItem.getUnitPrice());
    }
} 