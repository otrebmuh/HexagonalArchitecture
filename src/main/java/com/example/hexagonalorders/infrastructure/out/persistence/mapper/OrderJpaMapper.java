package com.example.hexagonalorders.infrastructure.out.persistence.mapper;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OrderItem;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import com.example.hexagonalorders.domain.model.valueobject.ShippingAddress;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OrderJpaEntity;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OrderItemJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderJpaMapper {
    
    public OrderJpaEntity toJpaEntity(Order order) {
        OrderJpaEntity jpaEntity = new OrderJpaEntity();
        if (order.getId() != null) {
            jpaEntity.setId(order.getId());
        }
        
        // Handle null orderNumber for new orders
        if (order.getOrderNumber() != null) {
            jpaEntity.setOrderNumber(order.getOrderNumber().value());
        } else {
            // For new orders, we'll set a placeholder that will be updated later
            jpaEntity.setOrderNumber("TEMP-" + System.currentTimeMillis());
        }
        
        jpaEntity.setCustomerId(order.getCustomerId());
        jpaEntity.setOrderDate(order.getOrderDate());
        jpaEntity.setStatus(toJpaOrderStatus(order.getStatus()));
        // Map shipping address fields
        ShippingAddress shippingAddress = order.getShippingAddress();
        if (shippingAddress != null) {
            jpaEntity.setStreet(shippingAddress.getStreet());
            jpaEntity.setCity(shippingAddress.getCity());
            jpaEntity.setState(shippingAddress.getState());
            jpaEntity.setPostalCode(shippingAddress.getPostalCode());
            jpaEntity.setCountry(shippingAddress.getCountry());
        }
        
        List<OrderItemJpaEntity> items = order.getItems().stream()
                .map(item -> toJpaEntity(item, jpaEntity))
                .collect(Collectors.toList());
        jpaEntity.setItems(items);
        
        return jpaEntity;
    }
    
    private OrderItemJpaEntity toJpaEntity(OrderItem item, OrderJpaEntity order) {
        OrderItemJpaEntity jpaEntity = new OrderItemJpaEntity();
        jpaEntity.setProductNumber(item.getProductNumber().value());
        jpaEntity.setQuantity(item.getQuantity().value());
        jpaEntity.setUnitPrice(item.getUnitPrice());
        jpaEntity.setOrder(order); // Set the order reference
        return jpaEntity;
    }
    
    public Order toDomain(OrderJpaEntity jpaEntity) {
        List<OrderItem> items = jpaEntity.getItems().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
        ShippingAddress shippingAddress = new ShippingAddress(
            jpaEntity.getStreet(),
            jpaEntity.getCity(),
            jpaEntity.getState(),
            jpaEntity.getPostalCode(),
            jpaEntity.getCountry()
        );
        return new Order(
            jpaEntity.getId(),
            new OrderNumber(jpaEntity.getOrderNumber()),
            jpaEntity.getCustomerId(),
            jpaEntity.getOrderDate(),
            items,
            shippingAddress,
            toDomainOrderStatus(jpaEntity.getStatus())
        );
    }
    
    private OrderItem toDomain(OrderItemJpaEntity jpaEntity) {
        return new OrderItem(
            new ProductNumber(jpaEntity.getProductNumber()),
            new Quantity(jpaEntity.getQuantity()),
            jpaEntity.getUnitPrice()
        );
    }

    private com.example.hexagonalorders.infrastructure.out.persistence.entity.OrderStatus toJpaOrderStatus(com.example.hexagonalorders.domain.model.OrderStatus status) {
        return com.example.hexagonalorders.infrastructure.out.persistence.entity.OrderStatus.valueOf(status.name());
    }

    private com.example.hexagonalorders.domain.model.OrderStatus toDomainOrderStatus(com.example.hexagonalorders.infrastructure.out.persistence.entity.OrderStatus status) {
        return com.example.hexagonalorders.domain.model.OrderStatus.valueOf(status.name());
    }
} 