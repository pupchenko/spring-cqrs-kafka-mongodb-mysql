package com.practice.cqrs.core.infrastructure;

import com.practice.cqrs.core.events.BaseEvent;

import java.util.List;

public interface EventStore {
    void saveEvents(String aggregateId, Iterable<BaseEvent> evets, int expectedVersion);
    List<BaseEvent> getEvents(String aggregateId);

    List<String> getAggregateIds();
}
