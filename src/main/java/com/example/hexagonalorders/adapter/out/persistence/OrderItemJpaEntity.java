package com.example.hexagonalorders.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * JPA entity representing the database structure for order items.
 * This class is part of the adapter layer and is responsible for
 * mapping the OrderItem domain entity to the database schema.
 * 
 * It uses JPA annotations to define:
 * - Table name and structure
 * - Primary key generation strategy
 * - Many-to-one relationship with OrderJpaEntity
 * - Column mappings
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItemJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String productNumber;
    private Integer quantity;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderJpaEntity order;
} 