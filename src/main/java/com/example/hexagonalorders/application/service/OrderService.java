package com.example.hexagonalorders.application.service;

import com.example.hexagonalorders.domain.event.DomainEvent;
import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.port.in.OrderUseCase;
import com.example.hexagonalorders.domain.port.out.OrderNumberGenerator;
import com.example.hexagonalorders.domain.port.out.OrderRepository;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import com.example.hexagonalorders.domain.service.OrderValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Application service implementing the order-related use cases.
 * This class orchestrates domain logic and coordinates with output ports.
 * It is part of the application layer and is responsible for:
 * - Implementing use cases defined by the domain
 * - Coordinating between domain objects and services
 * - Managing transactions and use case flow
 */
public class OrderService implements OrderUseCase {
    private final OrderRepository orderRepository;
    private final OrderNumberGenerator orderNumberGenerator;
    private final OrderValidationService orderValidationService;
    private final ApplicationEventPublisher eventPublisher;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public OrderService(OrderRepository orderRepository, 
                       OrderNumberGenerator orderNumberGenerator, 
                       OrderValidationService orderValidationService, 
                       ApplicationEventPublisher eventPublisher, 
                       OutboxRepository outboxRepository, 
                       ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.orderNumberGenerator = orderNumberGenerator;
        this.orderValidationService = orderValidationService;
        this.eventPublisher = eventPublisher;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

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
            order.getStatus()
        );
        Order savedOrder = orderRepository.save(orderWithNumber);

        // Process domain events - publish internally and persist to outbox
        for (DomainEvent event : savedOrder.getDomainEvents()) {
            // Publish internally via Spring's event system
            eventPublisher.publishEvent(event);
           
            // Persist to outbox for reliable processing
            persistToOutbox(event, "Order", orderNumber.value());
        }
       
        savedOrder.clearDomainEvents();

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
    public void deleteOrder(OrderNumber orderNumber) {
        orderRepository.deleteByOrderNumber(orderNumber);
    }

    /**
     * Persists a domain event to the outbox.
     *
     * @param event the domain event
     * @param aggregateType the type of aggregate that produced the event
     * @param aggregateId the identifier of the aggregate
     */
    protected void persistToOutbox(DomainEvent event, String aggregateType, String aggregateId) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            String eventType = event.getClass().getSimpleName();
           
            // Generate a deterministic UUID based on the aggregateId string
            // This ensures the same aggregate always gets the same UUID
            UUID uuid = UUID.nameUUIDFromBytes(aggregateId.getBytes());
           
            OutboxMessage outboxMessage = OutboxMessage.createPendingMessage(
                aggregateType,
                uuid,
                eventType,
                payload
            );
           
            outboxRepository.save(outboxMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist event to outbox", e);
        }
    }
} 