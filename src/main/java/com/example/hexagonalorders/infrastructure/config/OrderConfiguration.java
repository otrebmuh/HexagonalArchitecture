package com.example.hexagonalorders.infrastructure.config;

import com.example.hexagonalorders.application.service.OrderService;
import com.example.hexagonalorders.domain.service.OrderValidationService;
import com.example.hexagonalorders.domain.port.out.OrderNumberGenerator;
import com.example.hexagonalorders.domain.port.out.OrderRepository;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for order-related beans.
 * This class is part of the infrastructure layer and is responsible for:
 * - Wiring together domain, application, and adapter components
 * - Managing infrastructure concerns
 * - Providing Spring beans for dependency injection
 */
@Configuration
public class OrderConfiguration {

    @Bean
    public OrderService orderService(
            OrderRepository orderRepository,
            OrderNumberGenerator orderNumberGenerator,
            OrderValidationService orderValidationService,
            ApplicationEventPublisher eventPublisher,
            OutboxRepository outboxRepository,
            ObjectMapper objectMapper) {
        return new OrderService(orderRepository, orderNumberGenerator, orderValidationService, eventPublisher, outboxRepository, objectMapper);
    }

    @Bean
    public OrderValidationService orderValidationService() {
        return new OrderValidationService();
    }
} 