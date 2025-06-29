package com.example.hexagonalorders.infrastructure.out.messaging;

import com.example.hexagonalorders.domain.port.out.MessagePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
* No-operation implementation of MessagePublisher that just logs messages.
* This allows the application to run without a real message broker.
*/
@Component
public class NoOpMessagePublisher implements MessagePublisher {
  
   private static final Logger log = LoggerFactory.getLogger(NoOpMessagePublisher.class);
  
   @Override
   public void publish(String topic, String payload) {
       // Aquí iría código para publicar a Kafka u otro bus de mensajes
       log.info("Would publish message to topic '{}': {}", topic, payload);
   }
} 