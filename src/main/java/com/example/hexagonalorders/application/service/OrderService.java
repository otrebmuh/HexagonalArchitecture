package com.example.hexagonalorders.application.service;

import com.example.hexagonalorders.application.exception.OrderNotFoundException;
import com.example.hexagonalorders.domain.event.DomainEvent;
import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.port.in.OrderUseCase;
import com.example.hexagonalorders.domain.port.out.OrderNumberGenerator;
import com.example.hexagonalorders.domain.port.out.OrderRepository;
import com.example.hexagonalorders.domain.service.OrderValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

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
@Slf4j
@RequiredArgsConstructor
public class OrderService implements OrderUseCase {
    private final OrderRepository orderRepository;
    private final OrderNumberGenerator orderNumberGenerator;
    private final OrderValidationService orderValidationService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Order createOrder(Order order) {
        // Validate the order using the domain service
        orderValidationService.validateOrder(order);
        
        OrderNumber orderNumber = orderNumberGenerator.generate();
        Order orderWithNumber = new Order(
            orderNumber,
            order.getCustomerId(),
            order.getOrderDate(),
            order.getItems(),
            order.getShippingAddress(),
            order.getStatus()
        );
        Order savedOrder = orderRepository.save(orderWithNumber);

        // Process domain events - publish internally
        // The DomainEventHandler will automatically handle integration event mapping if needed
        publishDomainEvents(orderWithNumber, orderNumber.value());

        return savedOrder;
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
    @Transactional
    public Order confirmOrder(OrderNumber orderNumber) {
        Optional<Order> orderOpt = orderRepository.findByOrderNumber(orderNumber);
        if (orderOpt.isEmpty()) {
            throw new OrderNotFoundException(orderNumber);
        }
        
        Order order = orderOpt.get();
        
        // Confirm the order (this will raise the OrderConfirmedEvent)
        order.confirm(order.getId());
        
        // Save the updated order
        Order savedOrder = orderRepository.save(order);
        
        // Process domain events - publish internally
        // The DomainEventHandler will automatically handle integration event mapping
        publishDomainEvents(order, orderNumber.value());
        
        return savedOrder;
    }

    @Override
    @Transactional
    public void deleteOrder(OrderNumber orderNumber) {
        orderRepository.deleteByOrderNumber(orderNumber);
    }
    
    /**
     * Publishes domain events from an order and clears them afterward.
     * This method handles the common logic for publishing domain events
     * that is used in both createOrder and confirmOrder methods.
     * 
     * @param order the order containing domain events to publish
     * @param orderNumberValue the order number value for logging purposes
     */
    private void publishDomainEvents(Order order, String orderNumberValue) {
        log.debug("📤 ORDER SERVICE: About to publish {} domain events for order: {}", 
                 order.getDomainEvents().size(), orderNumberValue);
        for (DomainEvent event : order.getDomainEvents()) {
            log.debug("📤 ORDER SERVICE: Publishing domain event: {} for order: {}", 
                     event.getClass().getSimpleName(), orderNumberValue);
            // Publish internally via Spring's event system
            eventPublisher.publishEvent(event);
            log.debug("📤 ORDER SERVICE: Successfully published event: {}", event.getClass().getSimpleName());
        }
        
        order.clearDomainEvents();
    }
} 