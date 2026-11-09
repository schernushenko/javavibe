package com.csv.javavibe.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topicName;

    public HelloService(KafkaTemplate<String, String> kafkaTemplate,
                        @Value("${spring.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    /**
     * Returns a greeting message.
     * @return The greeting string.
     */
    public String getGreeting() {
        return "Hello World from Service!";
    }

    /**
     * Sends a text message to Kafka.
     * @param message The message content.
     */
    public void sendMessageToKafka(String message) {
        kafkaTemplate.send(topicName, message);
    }
}