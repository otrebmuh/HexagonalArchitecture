package com.example.hexagonalorders.adapter.in.web;

import com.example.hexagonalorders.application.service.OrderService;
import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.application.port.in.OrderUseCase;
import com.example.hexagonalorders.adapter.in.web.dto.OrderDto;
import com.example.hexagonalorders.adapter.in.web.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        Order order = orderMapper.toDomain(orderDto);
        Order savedOrder = orderService.createOrder(order);
        return ResponseEntity.ok(orderMapper.toDto(savedOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable Long id) {
        return orderService.getOrder(id)
                .map(orderMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
} 