package com.example.hexagonalorders.domain.port.in;

import com.example.hexagonalorders.infrastructure.in.web.dto.OrderDto;
import org.springframework.http.ResponseEntity;

/**
 * Input port defining the contract for the web interface of order operations.
 * This interface is part of the domain layer and defines how the web adapter
 * should interact with the application core.
 * 
 * The domain layer defines this contract to maintain its independence from
 * the delivery mechanism (REST API in this case).
 */
public interface OrderControllerPort {
    ResponseEntity<OrderDto> createOrder(OrderDto orderDto);
    ResponseEntity<OrderDto> getOrder(String orderNumber);
    ResponseEntity<Void> deleteOrder(String orderNumber);
} 