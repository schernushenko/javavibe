package com.csv.javavibe.controller;

import com.csv.javavibe.dto.OrderEvent;
import com.csv.javavibe.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/flash-sale")
@Tag(name = "Flash Sale API", description = "Эндпоинты для высоконагруженной распродажи")
public class FlashSaleController {

    private final InventoryService inventoryService;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private static final String TOPIC = "flash-sale-orders";

    public FlashSaleController(InventoryService inventoryService, KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.inventoryService = inventoryService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/buy")
    @Operation(summary = "Купить билет", description = "Проверяет остаток в Redis и отправляет событие в Kafka")
    public ResponseEntity<String> buyTicket(@RequestParam String email) {
        // 1. Пытаемся зарезервировать билет в Redis (быстро)
        if (!inventoryService.tryReserveTicket()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("SOLD OUT! Билеты закончились.");
        }

        // 2. Если билет есть, создаем событие и отправляем в Kafka (асинхронно)
        OrderEvent event = new OrderEvent(
                Math.abs(UUID.randomUUID().getMostSignificantBits()),
                "Concert Ticket",
                1,
                email
        );

        kafkaTemplate.send(TOPIC, event);

        // 3. Возвращаем успех пользователю (БД запишется потом)
        return ResponseEntity.ok("SUCCESS! Заказ в обработке для " + email);
    }
}