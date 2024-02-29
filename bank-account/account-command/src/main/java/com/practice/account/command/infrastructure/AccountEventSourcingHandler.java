package com.practice.account.command.infrastructure;

import com.practice.account.command.domain.AccountAggregate;
import com.practice.cqrs.core.domain.AggregateRoot;
import com.practice.cqrs.core.events.BaseEvent;
import com.practice.cqrs.core.handlers.EventSourcingHandler;
import com.practice.cqrs.core.infrastructure.EventStore;
import com.practice.cqrs.core.producers.EventProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {
    private final EventStore eventStore;
    private final EventProducer eventProducer;

    @Value("${spring.kafka.topic}")
    private String topic;

    public AccountEventSourcingHandler(EventStore eventStore, EventProducer eventProducer) {
        this.eventStore = eventStore;
        this.eventProducer = eventProducer;
    }

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
        aggregate.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        var aggregate = new AccountAggregate();
        var events = eventStore.getEvents(id);
        if (events != null && !events.isEmpty()){
            aggregate.replayEvents(events);
            var latestVersion = events.stream().map(BaseEvent::getVersion).max(Comparator.naturalOrder());
            aggregate.setVersion(latestVersion.get());
        }
        return aggregate;
    }

    @Override
    public void republishEvents() {
        var aggregateIds = eventStore.getAggregateIds();
        for (var aggregateId : aggregateIds){
            var aggregate = getById(aggregateId);
            if (aggregate == null || !aggregate.getActive()) {
                continue;
            }
            var events = eventStore.getEvents(aggregateId);
            for (var event: events){
                eventProducer.produce(topic, event);
            }
        }
    }
}
