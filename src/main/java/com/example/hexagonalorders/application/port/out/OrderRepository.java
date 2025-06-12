package com.example.hexagonalorders.application.port.out;

import com.example.hexagonalorders.domain.model.Order;
import java.util.List;
import java.util.Optional;

/**
 * Output port defining the contract for order persistence operations.
 * This interface is part of the application layer and defines how the application
 * expects to interact with the persistence layer.
 * 
 * Implementations of this port should be provided by the adapter layer
 * (e.g., using JPA, MongoDB, etc.).
 */
public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    void deleteById(Long id);
} 