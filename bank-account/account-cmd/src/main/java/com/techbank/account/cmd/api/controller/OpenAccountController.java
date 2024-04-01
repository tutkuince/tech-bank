package com.techbank.account.cmd.api.controller;

import com.techbank.account.cmd.api.command.OpenAccountCommand;
import com.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/openBankAccount")
public class OpenAccountController {
    private final Logger logger = Logger.getLogger(OpenAccountController.class.getName());

    private final CommandDispatcher commandDispatcher;

    public OpenAccountController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PostMapping
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand command) {
         String id = UUID.randomUUID().toString();
        command.setId(id);
        try {
            commandDispatcher.send(command);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new OpenAccountResponse("Bank account creation request completed successfully!", id));
        } catch (IllegalStateException e) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", e.toString()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(e.toString()));
        } catch (Exception e) {
            String safeErrorMessage = MessageFormat.format("Error while processing request to open a new bank account for id - {0}.", id);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new OpenAccountResponse(safeErrorMessage, id));
        }
    }
}
