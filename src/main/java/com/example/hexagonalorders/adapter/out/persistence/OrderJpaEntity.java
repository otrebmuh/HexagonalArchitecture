package com.example.hexagonalorders.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity representing the database structure for orders.
 * This class is part of the adapter layer and is responsible for
 * mapping the Order domain entity to the database schema.
 * 
 * It uses JPA annotations to define:
 * - Table name and structure
 * - Primary key generation strategy
 * - Relationships with other entities
 * - Column mappings
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String orderNumber;
    private LocalDateTime creationDate;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemJpaEntity> items = new ArrayList<>();
} 