package com.practice.account.command.api.commands;

import com.practice.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class DepositFundsCommand extends BaseCommand {
    private double amount;
}
