package com.example.hexagonalorders.infrastructure.out.messaging;

import com.example.hexagonalorders.domain.port.out.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * No-operation implementation of MessagePublisher that just logs messages.
 * This is used for development and testing when Kafka is not available.
 * Only active when 'kafka.enabled' property is set to 'false'.
 */
@Component
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "false", matchIfMissing = false)
public class NoOpMessagePublisher implements MessagePublisher {
    
    private static final Logger log = LoggerFactory.getLogger(NoOpMessagePublisher.class);
    
    @Override
    public void publish(String topic, String payload) {
        log.info("NO-OP: Would publish message to topic '{}': {}", topic, payload);
        System.out.println("--> NO-OP: Would publish message to topic " + topic + " payload:" + payload);
    }
} 