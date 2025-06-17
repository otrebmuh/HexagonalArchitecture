package com.example.hexagonalorders.adapter.out.persistence;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OrderItem;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import com.example.hexagonalorders.application.port.out.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Secondary adapter implementing the OrderRepository port using JPA.
 * This class is part of the adapter layer and is responsible for:
 * - Converting between domain entities and JPA entities
 * - Implementing the persistence operations defined in the OrderRepository port
 * - Delegating actual database operations to JpaOrderRepository
 * 
 * This adapter follows the Adapter pattern to translate between the domain model
 * and the persistence model, keeping them decoupled.
 */
@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {
    private final JpaOrderRepository jpaOrderRepository;

    @Override
    public Order save(Order order) {
        OrderJpaEntity jpaEntity = toJpaEntity(order);
        OrderJpaEntity savedEntity = jpaOrderRepository.save(jpaEntity);
        return toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaOrderRepository.findById(id)
                .map(this::toDomainEntity);
    }

    @Override
    public List<Order> findAll() {
        return jpaOrderRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaOrderRepository.deleteById(id);
    }

    private OrderJpaEntity toJpaEntity(Order order) {
        OrderJpaEntity jpaEntity = new OrderJpaEntity();
        jpaEntity.setId(order.getId());
        jpaEntity.setOrderNumber(order.getOrderNumber().value());
        jpaEntity.setCreationDate(order.getCreationDate());
        
        List<OrderItemJpaEntity> jpaItems = order.getItems().stream()
                .map(item -> {
                    OrderItemJpaEntity jpaItem = new OrderItemJpaEntity();
                    jpaItem.setId(item.getId());
                    jpaItem.setProductNumber(item.getProductNumber().value());
                    jpaItem.setQuantity(item.getQuantity().value());
                    jpaItem.setOrder(jpaEntity);
                    return jpaItem;
                })
                .collect(Collectors.toList());
        
        jpaEntity.setItems(jpaItems);
        return jpaEntity;
    }

    private Order toDomainEntity(OrderJpaEntity jpaEntity) {
        Order order = new Order();
        order.setId(jpaEntity.getId());
        order.setOrderNumber(new OrderNumber(jpaEntity.getOrderNumber()));
        order.setCreationDate(jpaEntity.getCreationDate());
        
        List<OrderItem> domainItems = jpaEntity.getItems().stream()
                .map(jpaItem -> {
                    OrderItem item = new OrderItem();
                    item.setId(jpaItem.getId());
                    item.setProductNumber(new ProductNumber(jpaItem.getProductNumber()));
                    item.setQuantity(new Quantity(jpaItem.getQuantity()));
                    return item;
                })
                .collect(Collectors.toList());
        
        order.setItems(domainItems);
        return order;
    }
} 