package com.example.hexagonalorders.infrastructure.in.web.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * Data Transfer Object for OrderItem data.
 * This class is used to transfer order item data between the adapter layer
 * and the application layer, keeping the domain model isolated.
 */
@Data
public class OrderItemDto {
    private Long id;
    private String productNumber;
    private Integer quantity;
    private BigDecimal unitPrice;

    public OrderItemDto() {}

    public OrderItemDto(String productNumber, Integer quantity, BigDecimal unitPrice) {
        this.productNumber = productNumber;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
} 