package com.example.hexagonalorders.infrastructure.out.persistence.repository;

import com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for OutboxJpaEntity.
 */
public interface OutboxMessageJpaRepository extends JpaRepository<OutboxJpaEntity, UUID> {
    
    /**
     * Finds the top N outbox messages with the given status, ordered by creation time ascending.
     * 
     * @param status the status to filter by
     * @param pageable pagination information
     * @return list of outbox messages
     */
    List<OutboxJpaEntity> findByStatusOrderByCreatedAtAsc(OutboxJpaEntity.OutboxStatusJpa status, Pageable pageable);
} 