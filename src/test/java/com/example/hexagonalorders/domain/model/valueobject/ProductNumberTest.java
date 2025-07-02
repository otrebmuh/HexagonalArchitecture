package com.example.hexagonalorders.domain.model.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductNumberTest {

    @Test
    void shouldCreateProductNumberWithValidValue() {
        // Given
        String validValue = "PROD-001";

        // When
        ProductNumber productNumber = new ProductNumber(validValue);

        // Then
        assertNotNull(productNumber);
        assertEquals(validValue, productNumber.value());
    }

    @Test
    void shouldCreateProductNumberWithComplexValue() {
        // Given
        String complexValue = "PRODUCT-2024-001-ABC123";

        // When
        ProductNumber productNumber = new ProductNumber(complexValue);

        // Then
        assertNotNull(productNumber);
        assertEquals(complexValue, productNumber.value());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ProductNumber(null)
        );
        assertEquals("Product number cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValueIsEmpty() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ProductNumber("")
        );
        assertEquals("Product number cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenValueIsBlank() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new ProductNumber("   ")
        );
        assertEquals("Product number cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        // Given
        String value = "PROD-001";
        ProductNumber productNumber = new ProductNumber(value);

        // When
        String stringRepresentation = productNumber.toString();

        // Then
        assertEquals(value, stringRepresentation);
    }

    @Test
    void shouldBeEqualWhenValuesAreEqual() {
        // Given
        ProductNumber productNumber1 = new ProductNumber("PROD-001");
        ProductNumber productNumber2 = new ProductNumber("PROD-001");

        // When & Then
        assertEquals(productNumber1, productNumber2);
        assertEquals(productNumber1.hashCode(), productNumber2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        // Given
        ProductNumber productNumber1 = new ProductNumber("PROD-001");
        ProductNumber productNumber2 = new ProductNumber("PROD-002");

        // When & Then
        assertNotEquals(productNumber1, productNumber2);
    }

    @Test
    void shouldNotBeEqualWhenComparedWithDifferentType() {
        // Given
        ProductNumber productNumber = new ProductNumber("PROD-001");
        String differentType = "PROD-001";

        // When & Then
        assertNotEquals(productNumber, differentType);
    }
} 