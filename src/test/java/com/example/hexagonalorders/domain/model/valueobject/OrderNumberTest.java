package com.example.hexagonalorders.domain.model.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderNumberTest {

    @Test
    void shouldCreateOrderNumberWithValidValue() {
        // Given
        String validValue = "ORD-001";

        // When
        OrderNumber orderNumber = new OrderNumber(validValue);

        // Then
        assertNotNull(orderNumber);
        assertEquals(validValue, orderNumber.value());
    }

    @Test
    void shouldCreateOrderNumberWithComplexValue() {
        // Given
        String complexValue = "ORDER-2024-001-ABC123";

        // When
        OrderNumber orderNumber = new OrderNumber(complexValue);

        // Then
        assertNotNull(orderNumber);
        assertEquals(complexValue, orderNumber.value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new OrderNumber(null)
        );
        assertEquals("Order number cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValueIsEmpty() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new OrderNumber("")
        );
        assertEquals("Order number cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new OrderNumber("   ")
        );
        assertEquals("Order number cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        // Given
        String value = "ORD-001";
        OrderNumber orderNumber = new OrderNumber(value);

        // When
        String stringRepresentation = orderNumber.toString();

        // Then
        assertEquals(value, stringRepresentation);
    }

    @Test
    void shouldBeEqualWhenValuesAreEqual() {
        // Given
        OrderNumber orderNumber1 = new OrderNumber("ORD-001");
        OrderNumber orderNumber2 = new OrderNumber("ORD-001");

        // When & Then
        assertEquals(orderNumber1, orderNumber2);
        assertEquals(orderNumber1.hashCode(), orderNumber2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        // Given
        OrderNumber orderNumber1 = new OrderNumber("ORD-001");
        OrderNumber orderNumber2 = new OrderNumber("ORD-002");

        // When & Then
        assertNotEquals(orderNumber1, orderNumber2);
    }

    @Test
    void shouldNotBeEqualWhenComparedWithDifferentType() {
        // Given
        OrderNumber orderNumber = new OrderNumber("ORD-001");
        String differentType = "ORD-001";

        // When & Then
        assertNotEquals(orderNumber, differentType);
    }
} 