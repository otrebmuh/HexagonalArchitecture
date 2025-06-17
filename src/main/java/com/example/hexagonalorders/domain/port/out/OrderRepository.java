package com.example.hexagonalorders.domain.port.out;

import com.example.hexagonalorders.domain.model.Order;
import com.example.hexagonalorders.domain.model.valueobject.OrderNumber;

import java.util.List;
import java.util.Optional;

/**
 * Output port defining the contract for order persistence operations.
 * This interface is part of the domain layer and defines how the domain
 * expects to interact with the persistence layer.
 * 
 * Implementations of this port should be provided by the adapter layer
 * (e.g., using JPA, MongoDB, etc.).
 */
public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findByOrderNumber(OrderNumber orderNumber);
    List<Order> findAll();
    void deleteByOrderNumber(OrderNumber orderNumber);
} 