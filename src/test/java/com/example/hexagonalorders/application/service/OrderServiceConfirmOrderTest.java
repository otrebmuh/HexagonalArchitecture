package com.example.hexagonalorders.application.service;

import com.example.hexagonalorders.application.event.OrderConfirmedIntegrationEvent;
import com.example.hexagonalorders.application.exception.OrderNotFoundException;
import com.example.hexagonalorders.domain.event.OrderConfirmedEvent;
import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OrderStatus;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ShippingAddress;
import com.example.hexagonalorders.domain.port.out.OrderRepository;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import com.example.hexagonalorders.domain.service.OrderValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceConfirmOrderTest {

    @Mock
    private OrderRepository orderRepository;
    
    @Mock
    private OrderValidationService orderValidationService;
    
    @Mock
    private ApplicationEventPublisher eventPublisher;
    
    @Mock
    private OutboxRepository outboxRepository;
    
    @Mock
    private ObjectMapper objectMapper;

    private OrderService orderService;

    private OrderNumber orderNumber;
    private Order pendingOrder;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(
            orderRepository,
            null, // OrderNumberGenerator not needed for this test
            orderValidationService,
            eventPublisher,
            outboxRepository,
            objectMapper
        );

        orderNumber = new OrderNumber("ORD-001");
        
        List<com.example.hexagonalorders.domain.model.OrderItem> items = new ArrayList<>();
        items.add(new com.example.hexagonalorders.domain.model.OrderItem(
            new com.example.hexagonalorders.domain.model.valueobject.ProductNumber("PROD-001"),
            new com.example.hexagonalorders.domain.model.valueobject.Quantity(2),
            new BigDecimal("29.99")
        ));
        
        ShippingAddress shippingAddress = new ShippingAddress("123 Main St", "City", "State", "12345", "Country");
        
        pendingOrder = new Order(orderNumber, "CUST-001", LocalDateTime.now(), items, shippingAddress, OrderStatus.PENDING);
    }

    @Test
    void shouldConfirmOrderSuccessfully() throws Exception {
        // Given
        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(pendingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            // The saved order should have the confirmed status and the OrderConfirmedEvent
            return savedOrder;
        });
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"orderNumber\":\"ORD-001\"}");

        // When
        Order result = orderService.confirmOrder(orderNumber);

        // Then
        assertNotNull(result);
        assertEquals(OrderStatus.CONFIRMED, result.getStatus());
        
        verify(orderRepository).findByOrderNumber(orderNumber);
        verify(orderRepository).save(any(Order.class));
        verify(eventPublisher).publishEvent(any(OrderConfirmedEvent.class));
        verify(outboxRepository).save(any());
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderDoesNotExist() {
        // Given
        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.empty());

        // When & Then
        OrderNotFoundException exception = assertThrows(
            OrderNotFoundException.class,
            () -> orderService.confirmOrder(orderNumber)
        );
        assertEquals("Order not found with number: ORD-001", exception.getMessage());
        
        verify(orderRepository).findByOrderNumber(orderNumber);
        verify(orderRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
        verify(outboxRepository, never()).save(any());
    }

    @Test
    void shouldPersistIntegrationEventToOutbox() throws Exception {
        // Given
        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Optional.of(pendingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            return savedOrder;
        });
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"orderNumber\":\"ORD-001\"}");

        ArgumentCaptor<com.example.hexagonalorders.domain.model.OutboxMessage> outboxCaptor = 
            ArgumentCaptor.forClass(com.example.hexagonalorders.domain.model.OutboxMessage.class);

        // When
        orderService.confirmOrder(orderNumber);

        // Then
        verify(outboxRepository).save(outboxCaptor.capture());
        com.example.hexagonalorders.domain.model.OutboxMessage savedMessage = outboxCaptor.getValue();
        assertEquals("OrderConfirmedIntegrationEvent", savedMessage.eventType());
        assertEquals("Order", savedMessage.aggregateType());
    }
} 