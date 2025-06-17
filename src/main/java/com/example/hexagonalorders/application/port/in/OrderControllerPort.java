package com.example.hexagonalorders.application.port.in;

import com.example.hexagonalorders.adapter.in.web.dto.OrderDto;
import org.springframework.http.ResponseEntity;
import java.util.List;

/**
 * Input port defining the contract for the web interface of order operations.
 * This interface is part of the application layer and defines how the web adapter
 * should interact with the application core.
 */
public interface OrderControllerPort {
    ResponseEntity<OrderDto> createOrder(OrderDto orderDto);
    ResponseEntity<OrderDto> getOrder(Long id);
    ResponseEntity<List<OrderDto>> getAllOrders();
    ResponseEntity<Void> deleteOrder(Long id);
} 