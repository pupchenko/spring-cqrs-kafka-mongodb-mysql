package com.practice.account.command.api.controllers;

import com.practice.account.command.api.commands.CloseAccountCommand;
import com.practice.account.command.api.commands.DepositFundsCommand;
import com.practice.account.command.api.commands.OpenAccountCommand;
import com.practice.account.command.api.commands.WithdrawFundsCommand;
import com.practice.account.command.api.dto.OpenAccountResponse;
import com.practice.account.common.dto.BaseResponse;
import com.practice.cqrs.core.exceptions.AggregateNotFoundException;
import com.practice.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/accounts")
public class AccountController {
    private final Logger logger = Logger.getLogger(AccountController.class.getName());

    private final CommandDispatcher commandDispatcher;

    public AccountController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand command){
        var id = UUID.randomUUID().toString();
        command.setId(id);
        try {
            commandDispatcher.sent(command);
            return new ResponseEntity<>(new OpenAccountResponse("Bank account creation request completed successfully!", id), HttpStatus.CREATED);
        } catch (IllegalStateException e){
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            var safeErrorMessage = MessageFormat.format("Error while processing request to open a new bank account for id - {0}", id);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new OpenAccountResponse(safeErrorMessage, id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/deposit/{id}")
    public ResponseEntity<BaseResponse> depositFunds(@PathVariable(value = "id") String id, @RequestBody DepositFundsCommand command){
        try {
            command.setId(id);
            commandDispatcher.sent(command);
            return new ResponseEntity<>(new BaseResponse("Deposit funds request completed successfully!"), HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException e){
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            var safeErrorMessage = MessageFormat.format("Error while processing request to deposit funds to bank account with id - {0}", id);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/withdraw/{id}")
    public ResponseEntity<BaseResponse> withdrawFunds(@PathVariable(value = "id") String id, @RequestBody WithdrawFundsCommand command){
        try {
            command.setId(id);
            commandDispatcher.sent(command);
            return new ResponseEntity<>(new BaseResponse("Withdraw funds request completed successfully!"), HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException e){
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            var safeErrorMessage = MessageFormat.format("Error while processing request to withdraw funds to bank account with id - {0}", id);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> closeAccount(@PathVariable(value = "id") String id){
        try {
            commandDispatcher.sent(new CloseAccountCommand(id));
            return new ResponseEntity<>(new BaseResponse("Bank account closure was completed successfully!"), HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException e){
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            var safeErrorMessage = MessageFormat.format("Error while processing request to close bank account with id - {0}", id);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
