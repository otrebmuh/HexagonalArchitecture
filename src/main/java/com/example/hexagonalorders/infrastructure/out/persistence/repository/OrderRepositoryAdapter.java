package com.example.hexagonalorders.infrastructure.out.persistence.repository;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OrderItem;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import com.example.hexagonalorders.domain.port.out.OrderRepository;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OrderJpaEntity;
import com.example.hexagonalorders.infrastructure.out.persistence.mapper.OrderJpaMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderRepositoryAdapter implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderJpaMapper orderJpaMapper;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository, OrderJpaMapper orderJpaMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderJpaMapper = orderJpaMapper;
    }

    @Override
    public Order save(Order order) {
        return orderJpaMapper.toDomain(orderJpaRepository.save(orderJpaMapper.toJpaEntity(order)));
    }

    @Override
    public Optional<Order> findByOrderNumber(OrderNumber orderNumber) {
        return orderJpaRepository.findByOrderNumber(orderNumber.value())
                .map(orderJpaMapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return orderJpaRepository.findAll().stream()
                .map(orderJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByOrderNumber(OrderNumber orderNumber) {
        orderJpaRepository.deleteByOrderNumber(orderNumber.value());
    }
} 