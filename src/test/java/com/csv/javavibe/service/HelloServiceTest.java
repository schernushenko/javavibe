package com.csv.javavibe.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for HelloService using Embedded Kafka.
 * This avoids mocking issues with KafkaTemplate on newer Java versions.
 */
@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "test-topic")
@DirtiesContext
class HelloServiceTest {

    @Autowired
    private HelloService helloService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void getGreeting_shouldReturnCorrectMessage() {
        // Act
        String greeting = helloService.getGreeting();

        // Assert
        assertThat(greeting).isEqualTo("Hello World from Service!");
    }

    @Test
    void sendMessageToKafka_shouldSendToKafka() {
        // Arrange
        String testMessage = "Test Message";

        // Act
        helloService.sendMessageToKafka(testMessage);

        // Assert - verify message was sent by consuming it
        String received = kafkaTemplate
            .receive("test-topic", 0, 5000)
            .value();
        
        assertThat(received).isEqualTo(testMessage);
    }
}