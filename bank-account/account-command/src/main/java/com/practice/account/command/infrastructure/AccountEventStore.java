package com.practice.account.command.infrastructure;

import com.practice.account.command.domain.AccountAggregate;
import com.practice.account.command.domain.EventStoreRepository;
import com.practice.cqrs.core.events.BaseEvent;
import com.practice.cqrs.core.events.EventModel;
import com.practice.cqrs.core.exceptions.AggregateNotFoundException;
import com.practice.cqrs.core.exceptions.ConcurrencyException;
import com.practice.cqrs.core.infrastructure.EventStore;
import com.practice.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AccountEventStore implements EventStore {
    private final EventStoreRepository eventStoreRepository;
    private final EventProducer eventProducer;

    @Value("${spring.kafka.topic}")
    private String topic;

    public AccountEventStore(EventStoreRepository eventStoreRepository, EventProducer eventProducer) {
        this.eventStoreRepository = eventStoreRepository;
        this.eventProducer = eventProducer;
    }

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        var eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (expectedVersion != -1 && eventStream.get(eventStream.size()-1).getVersion() != expectedVersion){
            throw new ConcurrencyException();

        }
        var version = expectedVersion;
        for (var event: events){
            version++;
            event.setVersion(version);
            var eventModel = EventModel.builder()
                    .timestamp(new Date())
                    .aggregateIdentifier(aggregateId)
                    .aggregateType(AccountAggregate.class.getTypeName())
                    .version(version)
                    .eventType(event.getClass().getTypeName())
                    .eventData(event)
                    .build();
            var persistedEvent = eventStoreRepository.save(eventModel);
            if (!persistedEvent.getId().isEmpty()){
                eventProducer.produce(topic, event);
            }
        }
    }

    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        var eventStream = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (eventStream == null  || eventStream.isEmpty()){
            throw new AggregateNotFoundException("Incorrect account ID provided");
        }
        return eventStream.stream().map(event -> event.getEventData()).toList();
    }

    @Override
    public List<String> getAggregateIds() {
        var eventStream = eventStoreRepository.findAll();
        if (eventStream == null || eventStream.isEmpty()){
            throw new IllegalStateException("Could not retrieve event stream from the event store!");
        }
        return eventStream.stream().map(EventModel::getAggregateIdentifier).distinct().toList();
    }
}
