package com.csv.javavibe.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/load-test")
public class LoadTestController {

    @PostMapping
    @Operation(summary = "Запустить нагрузочный тест", description = "Создает 5000 параллельных запросов на покупку")
    public ResponseEntity<String> runLoadTest() {
        int totalRequests = 5000;
        int threadPoolSize = 100; // Количество одновременных потоков

        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
        CountDownLatch latch = new CountDownLatch(totalRequests);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/api/flash-sale/buy?email=user_";

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalRequests; i++) {
            final int userId = i;
            executor.submit(() -> {
                try {
                    restTemplate.postForEntity(url + userId + "@test.com", null, String.class);
                } catch (Exception e) {
                    // Игнорируем ошибки 400 (SOLD OUT), так и должно быть
                } finally {
                    latch.countDown();
                }
            });
        }

        try {
            latch.await(); // Ждем завершения всех потоков
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        executor.shutdown();

        long endTime = System.currentTimeMillis();
        return ResponseEntity.ok("Тест завершен за " + (endTime - startTime) + " мс. Проверьте логи консьюмера!");
    }
}