package com.practice.account.command.domain;

import com.practice.account.command.api.commands.OpenAccountCommand;
import com.practice.account.common.events.AccountClosedEvent;
import com.practice.account.common.events.AccountOpenedEvent;
import com.practice.account.common.events.FundsDepositedEvent;
import com.practice.account.common.events.FundsWithdrawnEvent;
import com.practice.cqrs.core.domain.AggregateRoot;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    @Getter
    private Boolean active;
    @Getter
    private double balance;

    public AccountAggregate(OpenAccountCommand command){
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .createdDate(new Date())
                .accountType(command.getAccountType())
                .openingBalance(command.getOpeningBalance())
                .build());
    }

    public void apply(AccountOpenedEvent event) {
        this.id = event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    public void depositFunds(double amount){
        if (!this.active){
            throw new IllegalArgumentException("Funds cannot be deposited into a closed account");
        }
        if (amount <=0){
            throw new IllegalArgumentException("The deposit ammount nust be greater than 0!");
        }
        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsDepositedEvent event){
        this.id = event.getId();
        this.balance += event.getAmount();
    }

    public void withdrawFunds(double amount){
        if (!this.active){
            throw new IllegalArgumentException("Funds cannot be withdrawn into a closed account");
        }
        raiseEvent(FundsWithdrawnEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FundsWithdrawnEvent event){
        this.id = event.getId();
        this.balance -= event.getAmount();
    }

    public void closeAccount(){
        if (!this.active){
            throw new IllegalArgumentException("The bank account has already been closed!");
        }
        raiseEvent(AccountClosedEvent.builder()
                .id(this.id)
                .build());
    }

    public void apply(AccountClosedEvent event){
        this.id = event.getId();
        this.active = false;
    }
}
