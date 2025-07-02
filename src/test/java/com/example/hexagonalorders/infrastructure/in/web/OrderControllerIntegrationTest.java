package com.example.hexagonalorders.infrastructure.in.web;

import com.example.hexagonalorders.domain.model.OrderStatus;
import com.example.hexagonalorders.infrastructure.in.web.dto.OrderDto;
import com.example.hexagonalorders.infrastructure.in.web.dto.OrderItemDto;
import com.example.hexagonalorders.infrastructure.out.event.OutboxProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private OutboxProcessor outboxProcessor;
    
    @BeforeEach
    public void setUp() {
        // Register the JavaTimeModule to handle LocalDateTime serialization
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void createOrder_ShouldReturnCreatedOrder() throws Exception {
        // Arrange
        OrderDto inputDto = createOrderDto();

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderNumber").exists())
                .andExpect(jsonPath("$.customerId").value("CUST-001"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    public void createOrder_ShouldTriggerOutboxProcessing() throws Exception {
        // Arrange
        OrderDto inputDto = createOrderDto();

        // Act - Create an order
        MvcResult result = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andReturn();
        
        // Manually trigger outbox processing to verify messages are published
        outboxProcessor.processOutboxMessages();
        
        // The NoOpMessagePublisher should have logged messages to console
        // We can't easily verify console output in tests, but we can verify
        // that the outbox processing completed without errors
    }

    @Test
    public void getOrder_WithExistingOrderNumber_ShouldReturnOrder() throws Exception {
        // Arrange - First create an order
        OrderDto inputDto = createOrderDto();
        MvcResult result = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andReturn();
        
        String responseJson = result.getResponse().getContentAsString();
        OrderDto createdOrder = objectMapper.readValue(responseJson, OrderDto.class);
        String orderNumber = createdOrder.getOrderNumber();

        // Act & Assert - Then retrieve it
        mockMvc.perform(get("/api/orders/{orderNumber}", orderNumber))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderNumber").value(orderNumber))
                .andExpect(jsonPath("$.customerId").value("CUST-001"));
    }

    @Test
    public void getOrder_WithNonExistingOrderNumber_ShouldReturnNotFound() throws Exception {
        // Arrange
        String orderNumber = "ORD-NONEXISTENT";
        
        // Act & Assert
        mockMvc.perform(get("/api/orders/{orderNumber}", orderNumber))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteOrder_ShouldReturnNoContent() throws Exception {
        // Arrange - First create an order
        OrderDto inputDto = createOrderDto();
        MvcResult result = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andReturn();
        
        String responseJson = result.getResponse().getContentAsString();
        OrderDto createdOrder = objectMapper.readValue(responseJson, OrderDto.class);
        String orderNumber = createdOrder.getOrderNumber();

        // Act & Assert - Then delete it
        mockMvc.perform(delete("/api/orders/{orderNumber}", orderNumber))
                .andExpect(status().isNoContent());
        
        // Verify it's gone
        mockMvc.perform(get("/api/orders/{orderNumber}", orderNumber))
                .andExpect(status().isNotFound());
    }

    private OrderDto createOrderDto() {
        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setProductNumber("PROD-001");
        itemDto.setQuantity(2);
        itemDto.setUnitPrice(BigDecimal.valueOf(19.99));

        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId("CUST-001");
        orderDto.setItems(List.of(itemDto));
        orderDto.setStatus("PENDING");
        orderDto.setOrderDate(LocalDateTime.now());
        return orderDto;
    }
} 