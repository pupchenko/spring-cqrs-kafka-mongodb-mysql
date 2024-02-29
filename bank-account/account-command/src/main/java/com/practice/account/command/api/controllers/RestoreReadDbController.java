package com.practice.account.command.api.controllers;

import com.practice.account.command.api.commands.RestoreReadDbCommand;
import com.practice.account.common.dto.BaseResponse;
import com.practice.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/restore")
public class RestoreReadDbController {

    private final Logger logger = Logger.getLogger(RestoreReadDbController.class.getName());

    private final CommandDispatcher commandDispatcher;

    public RestoreReadDbController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> restoreReadDb(){
        try {
            commandDispatcher.sent(new RestoreReadDbCommand());
            return new ResponseEntity<>(new BaseResponse("Read database restored successfully!"), HttpStatus.CREATED);
        } catch (IllegalStateException e){
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            var safeErrorMessage = "Error while processing request to restore read database)";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
