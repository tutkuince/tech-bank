package com.techbank.account.cmd.api.controller;

import com.techbank.account.cmd.api.command.DepositFundsCommand;
import com.techbank.account.cmd.api.command.WithdrawFundsCommand;
import com.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.exception.AggregateNotFoundException;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/withdrawFunds")
public class WithdrawFundsController {
    private final Logger logger = Logger.getLogger(WithdrawFundsController.class.getName());

    private final CommandDispatcher commandDispatcher;

    public WithdrawFundsController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> withdrawFunds(@PathVariable String id, @RequestBody WithdrawFundsCommand command) {
        try {
            command.setId(id);
            commandDispatcher.send(command);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new BaseResponse("Withdraw funds request completed successfully!"));
        } catch (IllegalStateException | AggregateNotFoundException e) {
            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request - {0}", e.toString()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse(e.toString()));
        } catch (Exception e) {
            String safeErrorMessage = MessageFormat.format("Error while processing request to withdraw funds from bank account with id - {0}.", id);
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new OpenAccountResponse(safeErrorMessage, id));
        }
    }
}
