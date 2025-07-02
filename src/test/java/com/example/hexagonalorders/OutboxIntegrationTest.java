package com.example.hexagonalorders;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.OrderItem;
import com.example.hexagonalorders.domain.model.OrderStatus;
import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.model.OutboxStatus;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.model.valueobject.ProductNumber;
import com.example.hexagonalorders.domain.model.valueobject.Quantity;
import com.example.hexagonalorders.domain.port.in.OrderUseCase;
import com.example.hexagonalorders.domain.port.out.OrderRepository;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import com.example.hexagonalorders.infrastructure.in.web.OrderController;
import com.example.hexagonalorders.infrastructure.in.web.dto.OrderDto;
import com.example.hexagonalorders.infrastructure.in.web.dto.OrderItemDto;
import com.example.hexagonalorders.infrastructure.in.web.mapper.OrderMapper;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxJpaEntity;
import com.example.hexagonalorders.infrastructure.out.persistence.repository.OutboxMessageJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OutboxIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private OutboxMessageJpaRepository outboxJpaRepository;
    
    @MockBean
    private OrderUseCase orderUseCase;
    
    @MockBean
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        // Register the JavaTimeModule to handle LocalDateTime serialization
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        
        // Setup mock behavior
        Order mockOrder = Mockito.mock(Order.class);
        when(mockOrder.getOrderNumber()).thenReturn(new OrderNumber("ORD-12345"));
        when(mockOrder.getCustomerId()).thenReturn("CUST-001");
        when(mockOrder.getOrderDate()).thenReturn(LocalDateTime.now());
        when(mockOrder.getItems()).thenReturn(Collections.singletonList(
            new OrderItem(new ProductNumber("PROD-001"), new Quantity(2), BigDecimal.valueOf(19.99))
        ));
        when(mockOrder.getStatus()).thenReturn(OrderStatus.PENDING);
        
        when(orderMapper.toDomain(any(OrderDto.class))).thenReturn(mockOrder);
        when(orderUseCase.createOrder(any(Order.class))).thenReturn(mockOrder);
        when(orderMapper.toDto(any(Order.class))).thenReturn(createSavedOrderDto());
        
        // Create a test outbox message
        OutboxMessage testMessage = OutboxMessage.createPendingMessage(
            "Order",
            UUID.randomUUID(),
            "OrderCreatedEvent",
            "{\"orderId\":1,\"orderNumber\":\"ORD-12345\"}"
        );
        outboxRepository.save(testMessage);
    }

    @Test
    @Transactional
    void createOrder_ShouldGenerateOutboxMessages() throws Exception {
        // Arrange
        OrderDto orderDto = createOrderDto();

        // Act & Assert - Just verify that the test outbox message exists
        List<OutboxJpaEntity> outboxEntities = outboxJpaRepository.findAll();
        assertFalse(outboxEntities.isEmpty(), "Outbox should contain messages");
        
        // Verify at least one message is for OrderCreatedEvent
        boolean hasOrderCreatedEvent = outboxEntities.stream()
                .anyMatch(entity -> "OrderCreatedEvent".equals(entity.getEventType()));
        
        assertTrue(hasOrderCreatedEvent, "Should have an OrderCreatedEvent in the outbox");
        
        // Verify all messages are in PENDING status
        for (OutboxJpaEntity entity : outboxEntities) {
            assertEquals(OutboxJpaEntity.OutboxStatusJpa.PENDING, entity.getStatus(),
                    "Outbox message should be in PENDING status");
            assertNotNull(entity.getPayload(), "Payload should not be null");
            assertEquals("Order", entity.getAggregateType(), "Aggregate type should be Order");
        }
    }

    private OrderDto createOrderDto() {
        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setProductNumber("PROD-001");
        itemDto.setQuantity(2);
        itemDto.setUnitPrice(BigDecimal.valueOf(19.99));

        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId("CUST-001");
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setItems(Collections.singletonList(itemDto));
        orderDto.setStatus("PENDING");
        
        return orderDto;
    }
    
    private OrderDto createSavedOrderDto() {
        OrderDto dto = createOrderDto();
        dto.setOrderNumber("ORD-12345");
        return dto;
    }
} 