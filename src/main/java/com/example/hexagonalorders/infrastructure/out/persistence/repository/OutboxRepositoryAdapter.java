package com.example.hexagonalorders.infrastructure.out.persistence.repository;

import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.model.Status;
import com.example.hexagonalorders.domain.port.out.OutboxRepository;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxJpaEntity;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxMessageMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class OutboxRepositoryAdapter implements OutboxRepository {
    
    private final OutboxMessageJpaRepository outboxMessageJpaRepository;
    
    public OutboxRepositoryAdapter(OutboxMessageJpaRepository outboxMessageJpaRepository) {
        this.outboxMessageJpaRepository = outboxMessageJpaRepository;
    }
    
    @Override
    public void save(OutboxMessage outboxMessage) {
        OutboxJpaEntity jpaEntity = OutboxMessageMapper.toJpaEntity(outboxMessage);
        outboxMessageJpaRepository.save(jpaEntity);
    }
    
    @Override
    public List<OutboxMessage> findPending(int limit) {
        PageRequest pageRequest = PageRequest.of(0, limit);
        List<OutboxJpaEntity> jpaEntities = outboxMessageJpaRepository
            .findTopNByStatusOrderByCreatedAtAsc(OutboxJpaEntity.OutboxStatusJpa.PENDING, pageRequest);
        
        return jpaEntities.stream()
            .map(OutboxMessageMapper::toDomainModel)
            .collect(Collectors.toList());
    }
    
    @Override
    public void markProcessed(UUID id) {
        outboxMessageJpaRepository.findById(id).ifPresent(jpaEntity -> {
            jpaEntity.setStatus(OutboxJpaEntity.OutboxStatusJpa.PROCESSED);
            jpaEntity.setProcessedAt(Instant.now());
            outboxMessageJpaRepository.save(jpaEntity);
        });
    }
    
    @Override
    public void markFailed(UUID id) {
        outboxMessageJpaRepository.findById(id).ifPresent(jpaEntity -> {
            jpaEntity.setStatus(OutboxJpaEntity.OutboxStatusJpa.FAILED);
            jpaEntity.setProcessedAt(Instant.now());
            outboxMessageJpaRepository.save(jpaEntity);
        });
    }
} 