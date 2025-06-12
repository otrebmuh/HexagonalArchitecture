package com.example.hexagonalorders.application.port.in;

import com.example.hexagonalorders.domain.model.Order;
import java.util.List;
import java.util.Optional;

/**
 * Input port defining the contract for order-related use cases.
 * This interface is part of the application layer and defines the operations
 * that external actors (like REST controllers) can perform on orders.
 * 
 * This port follows the Interface Segregation Principle by exposing only
 * the necessary operations for order management.
 */
public interface OrderUseCase {
    Order createOrder(Order order);
    Optional<Order> getOrder(Long id);
    List<Order> getAllOrders();
    void deleteOrder(Long id);
} 