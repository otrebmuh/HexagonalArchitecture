package com.example.hexagonalorders.infrastructure.out.persistence.repository;

import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxJpaEntity;
import com.example.hexagonalorders.infrastructure.out.persistence.mapper.OutboxMessageMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter implementation of the OutboxRepository port.
 * This class bridges the domain layer with the JPA persistence infrastructure.
 */
@Repository
public class OutboxRepositoryAdapter implements OutboxRepository {

    private final OutboxMessageJpaRepository jpaRepository;
    private final OutboxMessageMapper mapper;

    public OutboxRepositoryAdapter(OutboxMessageJpaRepository jpaRepository, OutboxMessageMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public OutboxMessage save(OutboxMessage message) {
        OutboxJpaEntity entity = mapper.toJpaEntity(message);
        OutboxJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OutboxMessage> findPending(int limit) {
        List<OutboxJpaEntity> entities = jpaRepository.findByStatusOrderByCreatedAtAsc(
            OutboxJpaEntity.OutboxStatusJpa.PENDING, 
            PageRequest.of(0, limit)
        );
        
        return entities.stream()
            .map(mapper::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markProcessed(UUID id) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setStatus(OutboxJpaEntity.OutboxStatusJpa.PROCESSED);
            entity.setProcessedAt(Instant.now());
            jpaRepository.save(entity);
        });
    }

    @Override
    @Transactional
    public void markFailed(UUID id) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setStatus(OutboxJpaEntity.OutboxStatusJpa.FAILED);
            entity.setProcessedAt(Instant.now());
            jpaRepository.save(entity);
        });
    }
} 