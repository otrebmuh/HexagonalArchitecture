package com.example.hexagonalorders.adapter.in.web.dto;

import lombok.Data;

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
} 