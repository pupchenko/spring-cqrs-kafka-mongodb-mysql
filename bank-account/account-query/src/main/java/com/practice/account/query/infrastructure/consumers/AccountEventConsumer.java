package com.practice.account.query.infrastructure.consumers;

import com.practice.account.query.infrastructure.handlers.EventHandler;
import com.practice.cqrs.core.events.BaseEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class AccountEventConsumer implements EventConsumer{
    private final EventHandler eventHandler;

    public AccountEventConsumer(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @KafkaListener(topics = "${spring.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(@Payload BaseEvent event, Acknowledgment ack) {
        try {
            var eventHandlerMethod = eventHandler.getClass().getDeclaredMethod("on", event.getClass());
            eventHandlerMethod.setAccessible(true);
            eventHandlerMethod.invoke(eventHandler, event);
            ack.acknowledge();
        } catch (Exception e) {
            throw new RuntimeException("Error while consuming event", e);
        }
    }
}
