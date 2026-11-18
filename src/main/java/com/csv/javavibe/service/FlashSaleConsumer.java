package com.csv.javavibe.service;

import com.csv.javavibe.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FlashSaleConsumer {

    private static final Logger log = LoggerFactory.getLogger(FlashSaleConsumer.class);

    @KafkaListener(topics = "flash-sale-orders", groupId = "flash-sale-group")
    public void consumeOrder(OrderEvent order) {
        // Если в email есть слово "bad", имитируем ошибку БД
        if (order.getCustomerEmail().contains("bad")) {
            throw new RuntimeException("Имитация падения БД! Невозможно сохранить заказ для: " + order.getCustomerEmail());
        }

        try {
            Thread.sleep(50);
            log.info("💾 [DB] Сохранен заказ: ID={}, Email={}", order.getOrderId(), order.getCustomerEmail());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Слушатель для топика DLT (Мертвые письма)
    @KafkaListener(topics = "flash-sale-orders.DLT", groupId = "flash-sale-dlq-group")
    public void consumeDlq(OrderEvent order) {
        log.error("💀 [DLQ] Сообщение попало в мертвую очередь! Причина: ошибка БД. Заказ: ID={}, Email={}",
                order.getOrderId(), order.getCustomerEmail());
        // Здесь в реальности вы можете сохранить заказ в БД со статусом ERROR, чтобы потом разобрать вручную
    }
}