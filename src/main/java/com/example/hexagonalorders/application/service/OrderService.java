package com.example.hexagonalorders.application.service;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.port.in.OrderUseCase;
import com.example.hexagonalorders.domain.port.out.OrderNumberGenerator;
import com.example.hexagonalorders.domain.port.out.OrderRepository;
import com.example.hexagonalorders.domain.service.OrderValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service implementing the order-related use cases.
 * This class orchestrates domain logic and coordinates with output ports.
 * It is part of the application layer and is responsible for:
 * - Implementing use cases defined by the domain
 * - Coordinating between domain objects and services
 * - Managing transactions and use case flow
 */
@Service
@RequiredArgsConstructor
public class OrderService implements OrderUseCase {
    private final OrderRepository orderRepository;
    private final OrderNumberGenerator orderNumberGenerator;
    private final OrderValidationService orderValidationService;

    @Override
    public Order createOrder(Order order) {
        // Validate the order using the domain service
        orderValidationService.validateOrder(order);
        
        OrderNumber orderNumber = orderNumberGenerator.generate();
        Order orderWithNumber = new Order(
            orderNumber,
            order.customerName(),
            order.orderDate(),
            order.items(),
            order.status()
        );
        return orderRepository.save(orderWithNumber);
    }

    @Override
    public Optional<Order> getOrder(OrderNumber orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(OrderNumber orderNumber) {
        orderRepository.deleteByOrderNumber(orderNumber);
    }
} 