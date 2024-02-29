package com.practice.cqrs.core.handlers;

import com.practice.cqrs.core.domain.AggregateRoot;

public interface EventSourcingHandler<T> {
    void save(AggregateRoot aggregate);
    T getById(String Id);

    void republishEvents();
}
