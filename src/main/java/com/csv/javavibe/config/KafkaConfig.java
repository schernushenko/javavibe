package com.csv.javavibe.config;

import com.csv.javavibe.dto.OrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfig {

    /**
     * Настраиваем обработчик ошибок.
     * @param kafkaTemplate Шаблон для отправки сообщений в DLT.
     * @return DefaultErrorHandler, который делает 3 попытки с задержкой 1 сек, затем шлет в DLT.
     */
    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        // 1. Создаем recoverer (восстановитель), который будет отправлять сообщения в DLT
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate);

        // 2. Настраиваем политику повторов: 3 попытки, интервал 1000 мс (1 секунда)
        FixedBackOff backOff = new FixedBackOff(1000L, 3L);

        // 3. Возвращаем обработчик
        return new DefaultErrorHandler(recoverer, backOff);
    }
}