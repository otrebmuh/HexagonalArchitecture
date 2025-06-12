package com.example.hexagonalorders.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository interface for order persistence operations.
 * This interface is part of the adapter layer and extends Spring Data JPA's
 * JpaRepository to provide basic CRUD operations for OrderJpaEntity.
 * 
 * Spring Data JPA will automatically implement this interface at runtime,
 * providing a concrete implementation with all the basic database operations.
 */
@Repository
public interface JpaOrderRepository extends JpaRepository<OrderJpaEntity, Long> {
} 