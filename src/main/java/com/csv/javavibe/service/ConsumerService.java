package com.csv.javavibe.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    private static final Logger log = LoggerFactory.getLogger(ConsumerService.class);

    /**
     * Listens to messages from the configured Kafka topic.
     * The topic name is read from application.yml via spring.topic.name
     */
    @KafkaListener(topics = "${spring.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        log.info("Received message from Kafka: {}", message);
        // Here you can add your business logic to process the message
        processMessage(message);
    }

    /**
     * Process the received message.
     * Override or extend this method for custom processing logic.
     */
    protected void processMessage(String message) {
        log.debug("Processing message: {}", message);
        // Add your custom processing logic here
    }
}