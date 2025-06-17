package com.example.hexagonalorders.infrastructure.out.persistence.mapper;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OrderItem;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OrderJpaEntity;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OrderItemJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {
    
    public OrderJpaEntity toJpaEntity(Order order) {
        OrderJpaEntity jpaEntity = new OrderJpaEntity();
        jpaEntity.setOrderNumber(order.orderNumber().value());
        jpaEntity.setCustomerName(order.customerName());
        jpaEntity.setOrderDate(order.orderDate());
        jpaEntity.setStatus(toJpaOrderStatus(order.status()));
        
        List<OrderItemJpaEntity> items = order.items().stream()
                .map(this::toJpaEntity)
                .collect(Collectors.toList());
        jpaEntity.setItems(items);
        
        return jpaEntity;
    }
    
    private OrderItemJpaEntity toJpaEntity(OrderItem item) {
        OrderItemJpaEntity jpaEntity = new OrderItemJpaEntity();
        jpaEntity.setProductNumber(item.productNumber().value());
        jpaEntity.setQuantity(item.quantity().value());
        jpaEntity.setUnitPrice(item.unitPrice());
        return jpaEntity;
    }
    
    public Order toDomain(OrderJpaEntity jpaEntity) {
        List<OrderItem> items = jpaEntity.getItems().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
        
        return new Order(
            new OrderNumber(jpaEntity.getOrderNumber()),
            jpaEntity.getCustomerName(),
            jpaEntity.getOrderDate(),
            items,
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