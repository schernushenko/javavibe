package com.csv.javavibe.service;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final StringRedisTemplate redisTemplate;
    private static final String TICKET_KEY = "ticket:stock";

    public InventoryService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Инициализация 1000 билетов при старте приложения
    @PostConstruct
    public void initStock() {
        redisTemplate.opsForValue().set(TICKET_KEY, "1000");
    }

    /**
     * Атомарно пытается зарезервировать 1 билет.
     * @return true, если билет есть, false, если закончились.
     */
    public boolean tryReserveTicket() {
        Long remaining = redisTemplate.opsForValue().decrement(TICKET_KEY);
        if (remaining != null && remaining >= 0) {
            return true;
        } else {
            // Если ушли в минус, возвращаем обратно (хотя при DECR это маловероятно, но для чистоты)
            if (remaining != null && remaining < 0) {
                redisTemplate.opsForValue().increment(TICKET_KEY);
            }
            return false;
        }
    }
}