package com.example.hexagonalorders.adapter.in.web.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Order data.
 * This class is used to transfer order data between the adapter layer
 * and the application layer, keeping the domain model isolated.
 */
@Data
public class OrderDto {
    private Long id;
    private String orderNumber;
    private LocalDateTime creationDate;
    private List<OrderItemDto> items;
} 