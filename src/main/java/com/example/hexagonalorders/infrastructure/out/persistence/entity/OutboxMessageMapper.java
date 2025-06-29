package com.example.hexagonalorders.infrastructure.out.persistence.entity;

import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.model.Status;

public class OutboxMessageMapper {
    
    public static OutboxJpaEntity toJpaEntity(OutboxMessage outboxMessage) {
        OutboxJpaEntity jpaEntity = new OutboxJpaEntity();
        jpaEntity.setId(outboxMessage.id());
        jpaEntity.setAggregateType(outboxMessage.aggregateType());
        jpaEntity.setAggregateId(outboxMessage.aggregateId());
        jpaEntity.setEventType(outboxMessage.eventType());
        jpaEntity.setPayload(outboxMessage.payload());
        jpaEntity.setStatus(mapToJpaStatus(outboxMessage.status()));
        jpaEntity.setCreatedAt(outboxMessage.createdAt());
        jpaEntity.setProcessedAt(outboxMessage.processedAt());
        return jpaEntity;
    }
    
    public static OutboxMessage toDomainModel(OutboxJpaEntity jpaEntity) {
        return new OutboxMessage(
            jpaEntity.getId(),
            jpaEntity.getAggregateType(),
            jpaEntity.getAggregateId(),
            jpaEntity.getEventType(),
            jpaEntity.getPayload(),
            mapToDomainStatus(jpaEntity.getStatus()),
            jpaEntity.getCreatedAt(),
            jpaEntity.getProcessedAt()
        );
    }
    
    private static OutboxJpaEntity.OutboxStatusJpa mapToJpaStatus(Status domainStatus) {
        return switch (domainStatus) {
            case PENDING -> OutboxJpaEntity.OutboxStatusJpa.PENDING;
            case PROCESSED -> OutboxJpaEntity.OutboxStatusJpa.PROCESSED;
            case FAILED -> OutboxJpaEntity.OutboxStatusJpa.FAILED;
        };
    }
    
    private static Status mapToDomainStatus(OutboxJpaEntity.OutboxStatusJpa jpaStatus) {
        return switch (jpaStatus) {
            case PENDING -> Status.PENDING;
            case PROCESSED -> Status.PROCESSED;
            case FAILED -> Status.FAILED;
        };
    }
} 