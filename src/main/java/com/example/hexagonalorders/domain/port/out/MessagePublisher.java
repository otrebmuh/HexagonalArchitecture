package com.example.hexagonalorders.domain.port.out;

/**
 * Output port defining the contract for publishing messages to external systems.
 * This interface is part of the domain layer and defines how
 * the application core interacts with messaging infrastructure.
 */
public interface MessagePublisher {
    
    /**
     * Publishes a message to the specified topic
     * 
     * @param topic the destination topic
     * @param payload the message payload
     */
    void publish(String topic, String payload);
} 