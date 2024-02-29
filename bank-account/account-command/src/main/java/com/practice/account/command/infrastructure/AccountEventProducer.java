package com.practice.account.command.infrastructure;

import com.practice.cqrs.core.events.BaseEvent;
import com.practice.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountEventProducer implements EventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public AccountEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void produce(String topic, BaseEvent event) {
        this.kafkaTemplate.send(topic, event);
    }
}
