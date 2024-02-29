package com.practice.account.command.api.commands;

import com.practice.account.common.dto.AccountType;
import com.practice.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;
    private AccountType accountType;
    private  double openingBalance;
}
