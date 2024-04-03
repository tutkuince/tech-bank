package com.techbank.account.cmd.api.controller;

import com.techbank.account.cmd.api.command.CloseAccountCommand;
import com.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.exception.AggregateNotFoundException;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/closeBankAccount")
public class CloseAccountController {

    private final Logger logger = Logger.getLogger(CloseAccountController.class.getName());

    private final CommandDispatcher commandDispatcher;

    public CloseAccountController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> closeAccount(@PathVariable String id) {
        try {
            commandDispatcher.send(new CloseAccountCommand(id));
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse("Ban account closure request successfully completed!"));
        } catch (IllegalStateException | AggregateNotFoundException e) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", e.toString()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(e.toString()));
        } catch (Exception e) {
            String safeErrorMessage = MessageFormat.format("Error while processing request to close bank account with id - {0}.", id);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new OpenAccountResponse(safeErrorMessage, id));
        }
    }
}
