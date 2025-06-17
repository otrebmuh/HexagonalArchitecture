package com.example.hexagonalorders.application.service;

import com.example.hexagonalorders.application.port.in.OrderUseCase;
import com.example.hexagonalorders.application.port.out.OrderNumberGenerator;
import com.example.hexagonalorders.application.port.out.OrderRepository;
import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.service.OrderValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service implementing the order-related use cases.
 * This class orchestrates domain logic and coordinates with output ports.
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
        
        order.setOrderNumber(new OrderNumber(orderNumberGenerator.generateOrderNumber()));
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrder(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
} 