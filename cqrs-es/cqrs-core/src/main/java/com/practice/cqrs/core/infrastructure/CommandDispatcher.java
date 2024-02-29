package com.practice.cqrs.core.infrastructure;

import com.practice.cqrs.core.commands.BaseCommand;
import com.practice.cqrs.core.commands.CommandHandlerMethod;

public interface CommandDispatcher {
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handler);
    void sent(BaseCommand command);
}
