package com.csv.javavibe.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for HelloService.
 * We test the implementation directly as it has no external dependencies.
 */
@SpringBootTest
class HelloServiceTest {

    @Autowired
    private HelloService helloService;

    @Test
    void getGreeting_shouldReturnCorrectMessage() {
        // Act
        String greeting = helloService.getGreeting();

        // Assert
        assertThat(greeting).isEqualTo("Hello World from Service!");
    }
}