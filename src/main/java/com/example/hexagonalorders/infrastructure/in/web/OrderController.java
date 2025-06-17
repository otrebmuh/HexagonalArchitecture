package com.example.hexagonalorders.infrastructure.in.web;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;
import com.example.hexagonalorders.domain.port.in.OrderControllerPort;
import com.example.hexagonalorders.domain.port.in.OrderUseCase;
import com.example.hexagonalorders.infrastructure.in.web.dto.OrderDto;
import com.example.hexagonalorders.infrastructure.in.web.mapper.OrderMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Primary adapter implementing the web interface for order operations.
 * This class is part of the adapter layer and is responsible for:
 * - Exposing REST endpoints for order operations
 * - Converting HTTP requests to domain operations using DTOs
 * - Converting domain responses to HTTP responses using DTOs
 * 
 * The controller uses the OrderService (which implements OrderUseCase) to
 * perform the actual business operations, following the Hexagonal Architecture
 * principle of having the domain layer independent of the delivery mechanism.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController implements OrderControllerPort {
    private final OrderUseCase orderUseCase;
    private final OrderMapper orderMapper;

    public OrderController(OrderUseCase orderUseCase, OrderMapper orderMapper) {
        this.orderUseCase = orderUseCase;
        this.orderMapper = orderMapper;
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        Order order = orderMapper.toDomain(orderDto);
        Order savedOrder = orderUseCase.createOrder(order);
        return ResponseEntity.ok(orderMapper.toDto(savedOrder));
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable String orderNumber) {
        return orderUseCase.getOrder(new OrderNumber(orderNumber))
                .map(orderMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderUseCase.getAllOrders());
    }

    @DeleteMapping("/{orderNumber}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderNumber) {
        orderUseCase.deleteOrder(new OrderNumber(orderNumber));
        return ResponseEntity.noContent().build();
    }
} 