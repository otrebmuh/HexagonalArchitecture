package com.example.hexagonalorders.infrastructure.out.persistence.repository;

import com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxMessageJpaRepository extends JpaRepository<OutboxJpaEntity, java.util.UUID> {
    List<OutboxJpaEntity> findTopNByStatusOrderByCreatedAtAsc(OutboxJpaEntity.OutboxStatusJpa status, Pageable pageable);
} 