package com.example.hexagonalorders.infrastructure.in.web.dto;

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
    private String customerName;
    private LocalDateTime orderDate;
    private List<OrderItemDto> items;
    private String status;

    public OrderDto() {}

    public OrderDto(String orderNumber, String customerName, LocalDateTime orderDate, List<OrderItemDto> items, String status) {
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.items = items;
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public String getStatus() {
        return status;
    }
} 