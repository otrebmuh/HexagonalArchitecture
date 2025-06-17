package com.example.hexagonalorders.infrastructure.out.persistence.repository;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OrderItem;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import com.example.hexagonalorders.domain.port.out.OrderRepository;
import com.example.hexagonalorders.infrastructure.out.persistence.mapper.OrderMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryAdapter implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderMapper orderMapper;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository, OrderMapper orderMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Order save(Order order) {
        return orderMapper.toDomain(orderJpaRepository.save(orderMapper.toJpaEntity(order)));
    }

    @Override
    public Optional<Order> findByOrderNumber(OrderNumber orderNumber) {
        return orderJpaRepository.findByOrderNumber(orderNumber.value())
                .map(orderMapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return orderJpaRepository.findAll().stream()
                .map(orderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByOrderNumber(OrderNumber orderNumber) {
        orderJpaRepository.deleteByOrderNumber(orderNumber.value());
    }
} 