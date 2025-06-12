package com.example.hexagonalorders.adapter.in.web.mapper;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OrderItem;
import com.example.hexagonalorders.adapter.in.web.dto.OrderDto;
import com.example.hexagonalorders.adapter.in.web.dto.OrderItemDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for converting between domain entities and DTOs.
 * This class is part of the adapter layer and helps maintain
 * the separation between the domain model and the API layer.
 */
@Component
public class OrderMapper {
    
    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }
        
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCreationDate(order.getCreationDate());
        dto.setItems(toItemDtos(order.getItems()));
        return dto;
    }
    
    public Order toDomain(OrderDto dto) {
        if (dto == null) {
            return null;
        }
        
        Order order = new Order();
        order.setId(dto.getId());
        order.setOrderNumber(dto.getOrderNumber());
        order.setCreationDate(dto.getCreationDate());
        
        if (dto.getItems() != null) {
            dto.getItems().forEach(itemDto -> {
                OrderItem item = new OrderItem();
                item.setId(itemDto.getId());
                item.setProductNumber(itemDto.getProductNumber());
                item.setQuantity(itemDto.getQuantity());
                order.addItem(item);
            });
        }
        
        return order;
    }
    
    private List<OrderItemDto> toItemDtos(List<OrderItem> items) {
        if (items == null) {
            return null;
        }
        
        return items.stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }
    
    private OrderItemDto toItemDto(OrderItem item) {
        if (item == null) {
            return null;
        }
        
        OrderItemDto dto = new OrderItemDto();
        dto.setId(item.getId());
        dto.setProductNumber(item.getProductNumber());
        dto.setQuantity(item.getQuantity());
        return dto;
    }
} 