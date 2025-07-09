package com.example.hexagonalorders.domain.model.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    void shouldCreateQuantityWithValidValue() {
        // Given
        int validValue = 5;

        // When
        Quantity quantity = new Quantity(validValue);

        // Then
        assertNotNull(quantity);
        assertEquals(validValue, quantity.value());
    }

    @Test
    void shouldCreateQuantityWithMinimumValue() {
        // Given
        int minimumValue = 1;

        // When
        Quantity quantity = new Quantity(minimumValue);

        // Then
        assertNotNull(quantity);
        assertEquals(minimumValue, quantity.value());
    }

    @Test
    void shouldCreateQuantityWithLargeValue() {
        // Given
        int largeValue = 999999;

        // When
        Quantity quantity = new Quantity(largeValue);

        // Then
        assertNotNull(quantity);
        assertEquals(largeValue, quantity.value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsZero() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Quantity(0)
        );
        assertEquals("Quantity must be greater than zero", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNegative() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Quantity(-5)
        );
        assertEquals("Quantity must be greater than zero", exception.getMessage());
    }

    @Test
    void shouldAddTwoQuantities() {
        // Given
        Quantity quantity1 = new Quantity(5);
        Quantity quantity2 = new Quantity(3);

        // When
        Quantity result = quantity1.add(quantity2);

        // Then
        assertEquals(8, result.value());
    }

    @Test
    void shouldSubtractTwoQuantities() {
        // Given
        Quantity quantity1 = new Quantity(10);
        Quantity quantity2 = new Quantity(3);

        // When
        Quantity result = quantity1.subtract(quantity2);

        // Then
        assertEquals(7, result.value());
    }

    @Test
    void shouldThrowExceptionWhenSubtractionResultsInZero() {
        // Given
        Quantity quantity1 = new Quantity(5);
        Quantity quantity2 = new Quantity(5);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> quantity1.subtract(quantity2)
        );
        assertEquals("Resulting quantity must be greater than zero", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSubtractionResultsInNegative() {
        // Given
        Quantity quantity1 = new Quantity(3);
        Quantity quantity2 = new Quantity(5);

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> quantity1.subtract(quantity2)
        );
        assertEquals("Resulting quantity must be greater than zero", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        // Given
        int value = 42;
        Quantity quantity = new Quantity(value);

        // When
        String stringRepresentation = quantity.toString();

        // Then
        assertEquals("42", stringRepresentation);
    }

    @Test
    void shouldBeEqualWhenValuesAreEqual() {
        // Given
        Quantity quantity1 = new Quantity(5);
        Quantity quantity2 = new Quantity(5);

        // When & Then
        assertEquals(quantity1, quantity2);
        assertEquals(quantity1.hashCode(), quantity2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        // Given
        Quantity quantity1 = new Quantity(5);
        Quantity quantity2 = new Quantity(10);

        // When & Then
        assertNotEquals(quantity1, quantity2);
    }

    @Test
    void shouldNotBeEqualWhenComparedWithDifferentType() {
        // Given
        Quantity quantity = new Quantity(5);
        Integer differentType = 5;

        // When & Then
        assertNotEquals(quantity, differentType);
    }
} 