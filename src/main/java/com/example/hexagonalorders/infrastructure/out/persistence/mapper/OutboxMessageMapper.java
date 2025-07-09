package com.example.hexagonalorders.infrastructure.out.persistence.mapper;

import com.example.hexagonalorders.domain.model.OutboxMessage;
import com.example.hexagonalorders.domain.model.OutboxStatus;
import com.example.hexagonalorders.infrastructure.out.persistence.entity.OutboxJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper to convert between domain OutboxMessage and JPA OutboxJpaEntity.
 */
@Component
public class OutboxMessageMapper {

    /**
     * Maps a domain OutboxMessage to a JPA OutboxJpaEntity.
     * 
     * @param message the domain message
     * @return the JPA entity
     */
    public OutboxJpaEntity toJpaEntity(OutboxMessage message) {
        OutboxJpaEntity entity = new OutboxJpaEntity();
        entity.setId(message.id());
        entity.setAggregateType(message.aggregateType());
        entity.setAggregateId(message.aggregateId());
        entity.setEventType(message.eventType());
        entity.setPayload(message.payload());
        entity.setStatus(mapStatus(message.status()));
        entity.setCreatedAt(message.createdAt());
        entity.setProcessedAt(message.processedAt());
        return entity;
    }

    /**
     * Maps a JPA OutboxJpaEntity to a domain OutboxMessage.
     * 
     * @param entity the JPA entity
     * @return the domain message
     */
    public OutboxMessage toDomainEntity(OutboxJpaEntity entity) {
        return new OutboxMessage(
            entity.getId(),
            entity.getAggregateType(),
            entity.getAggregateId(),
            entity.getEventType(),
            entity.getPayload(),
            mapStatus(entity.getStatus()),
            entity.getCreatedAt(),
            entity.getProcessedAt()
        );
    }

    /**
     * Maps domain OutboxStatus to JPA OutboxStatusJpa.
     */
    private OutboxJpaEntity.OutboxStatusJpa mapStatus(OutboxStatus status) {
        return switch (status) {
            case PENDING -> OutboxJpaEntity.OutboxStatusJpa.PENDING;
            case PROCESSED -> OutboxJpaEntity.OutboxStatusJpa.PROCESSED;
            case FAILED -> OutboxJpaEntity.OutboxStatusJpa.FAILED;
        };
    }

    /**
     * Maps JPA OutboxStatusJpa to domain OutboxStatus.
     */
    private OutboxStatus mapStatus(OutboxJpaEntity.OutboxStatusJpa status) {
        return switch (status) {
            case PENDING -> OutboxStatus.PENDING;
            case PROCESSED -> OutboxStatus.PROCESSED;
            case FAILED -> OutboxStatus.FAILED;
        };
    }
} 