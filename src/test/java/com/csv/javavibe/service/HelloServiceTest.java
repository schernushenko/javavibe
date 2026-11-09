package com.csv.javavibe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;

class HelloServiceTest {

    @Test
    void getGreeting_shouldReturnExpectedGreeting() {
        CapturingKafkaTemplate kafkaTemplate = new CapturingKafkaTemplate();
        HelloService service = new HelloService(kafkaTemplate, "test-topic");

        String greeting = service.getGreeting();

        assertEquals("Hello World from Service!", greeting);
    }

    @Test
    void sendMessageToKafka_shouldSendMessageToConfiguredTopic() {
        CapturingKafkaTemplate kafkaTemplate = new CapturingKafkaTemplate();
        HelloService service = new HelloService(kafkaTemplate, "test-topic");

        service.sendMessageToKafka("hello");

        assertEquals("test-topic", kafkaTemplate.topic);
        assertEquals("hello", kafkaTemplate.message);
    }

    private static class CapturingKafkaTemplate extends KafkaTemplate<String, String> {
        private String topic;
        private String message;

        private CapturingKafkaTemplate() {
            super(new TestProducerFactory());
        }

        @Override
        public CompletableFuture<SendResult<String, String>> send(String topic, String data) {
            this.topic = topic;
            this.message = data;
            return CompletableFuture.completedFuture(null);
        }
    }

    private static class TestProducerFactory implements ProducerFactory<String, String> {
        @Override
        public org.apache.kafka.clients.producer.Producer<String, String> createProducer() {
            return null;
        }
    }
}
