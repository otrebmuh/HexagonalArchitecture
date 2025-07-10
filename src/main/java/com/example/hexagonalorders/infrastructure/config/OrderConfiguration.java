package com.example.hexagonalorders.infrastructure.config;

import com.example.hexagonalorders.application.service.OrderService;
import com.example.hexagonalorders.domain.service.OrderValidationService;
import com.example.hexagonalorders.domain.port.out.OrderNumberGenerator;
import com.example.hexagonalorders.domain.port.out.OrderRepository;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import com.example.hexagonalorders.domain.port.out.MessagePublisher;
import com.example.hexagonalorders.infrastructure.out.messaging.KafkaMessagePublisher;
import com.example.hexagonalorders.infrastructure.out.messaging.TopicNameMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            ApplicationEventPublisher eventPublisher) {
        return new OrderService(
            orderRepository, 
            orderNumberGenerator, 
            orderValidationService, 
            eventPublisher
        );
    }

    @Bean
    public OrderValidationService orderValidationService() {
        return new OrderValidationService();
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Register JavaTimeModule
        objectMapper.registerModule(new JavaTimeModule());
        
        // Configure LocalDateTime serialization format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        objectMapper.registerModule(new JavaTimeModule()
            .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter)));
        
        // Disable writing dates as timestamps
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        return objectMapper;
    }

    /**
     * Configures the MessagePublisher bean to use Kafka implementation.
     * This bean can be easily swapped for testing or different environments.
     */
    @Bean
    @Primary
    public MessagePublisher messagePublisher(KafkaTemplate<String, String> kafkaTemplate, TopicNameMapper topicNameMapper) {
        return new KafkaMessagePublisher(kafkaTemplate, topicNameMapper);
    }
} 