package com.practice.cqrs.core.producers;

import com.practice.cqrs.core.events.BaseEvent;

public interface EventProducer {
    void produce(String topic, BaseEvent event);
}
