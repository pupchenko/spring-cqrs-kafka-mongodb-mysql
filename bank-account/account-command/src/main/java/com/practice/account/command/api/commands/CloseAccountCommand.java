package com.practice.account.command.api.commands;

import com.practice.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class CloseAccountCommand extends BaseCommand {
    public CloseAccountCommand(String id){
        super(id);
    }
}
