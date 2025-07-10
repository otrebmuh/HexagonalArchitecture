package com.example.hexagonalorders.infrastructure.out.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service responsible for mapping domain events to Kafka topic names.
 * This provides a centralized place for topic naming logic and allows for
 * environment-specific topic prefixes and custom topic name mappings.
 */
@Component
public class TopicNameMapper {

    @Value("${kafka.topic.prefix:hexagonal-orders}")
    private String topicPrefix;

    @Value("${kafka.topic.environment:dev}")
    private String environment;

    // Map for custom topic name mappings
    private final Map<String, String> topicNameMappings = new ConcurrentHashMap<>();

    public TopicNameMapper() {
        // Initialize custom topic name mappings
        topicNameMappings.put("Order.OrderConfirmedIntegrationEvent", "order-confirmed");
        topicNameMappings.put("Order.OrderCreatedIntegrationEvent", "order-created");
        // Add more mappings as needed
    }

    /**
     * Maps an aggregate type and event type to a Kafka topic name.
     * 
     * @param aggregateType the type of aggregate (e.g., "Order")
     * @param eventType the type of event (e.g., "OrderConfirmedIntegrationEvent")
     * @return the Kafka topic name
     */
    public String mapToTopicName(String aggregateType, String eventType) {
        String fullEventName = aggregateType + "." + eventType;
        
        // Check if there's a custom mapping for this event
        String customTopicName = topicNameMappings.get(fullEventName);
        if (customTopicName != null) {
            return buildTopicName(customTopicName);
        }
        
        // Fallback to the default naming convention
        String defaultTopicName = aggregateType.toLowerCase() + "-" + 
                                 eventType.replace("IntegrationEvent", "").toLowerCase();
        return buildTopicName(defaultTopicName);
    }

    /**
     * Builds the full topic name with prefix and environment.
     * 
     * @param baseTopicName the base topic name
     * @return the full topic name
     */
    private String buildTopicName(String baseTopicName) {
        return String.format("%s-%s-%s", topicPrefix, environment, baseTopicName);
    }

    /**
     * Adds a custom topic name mapping.
     * 
     * @param eventName the full event name (aggregateType.eventType)
     * @param topicName the custom topic name
     */
    public void addTopicMapping(String eventName, String topicName) {
        topicNameMappings.put(eventName, topicName);
    }
} 