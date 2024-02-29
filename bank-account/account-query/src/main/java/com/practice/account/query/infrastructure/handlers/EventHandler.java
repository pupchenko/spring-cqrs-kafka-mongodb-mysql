package com.practice.account.query.infrastructure.handlers;

import com.practice.account.common.events.AccountClosedEvent;
import com.practice.account.common.events.AccountOpenedEvent;
import com.practice.account.common.events.FundsDepositedEvent;
import com.practice.account.common.events.FundsWithdrawnEvent;

public interface EventHandler {
    void on(AccountOpenedEvent event);
    void on(FundsDepositedEvent event);
    void on(FundsWithdrawnEvent event);
    void on(AccountClosedEvent event);
}
