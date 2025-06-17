package com.example.hexagonalorders.domain.port.in;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;

import java.util.List;
import java.util.Optional;

/**
 * Input port defining the contract for order-related use cases.
 * This interface is part of the domain layer and defines the operations
 * that external actors can perform on orders.
 * 
 * This port follows the Interface Segregation Principle by exposing only
 * the necessary operations for order management.
 */
public interface OrderUseCase {
    Order createOrder(Order order);
    Optional<Order> getOrder(OrderNumber orderNumber);
    List<Order> getAllOrders();
    void deleteOrder(OrderNumber orderNumber);
} 