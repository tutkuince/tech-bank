package com.techbank.account.query.api.controller;

import com.techbank.account.query.api.dto.AccountLookupResponse;
import com.techbank.account.query.api.dto.EqualityType;
import com.techbank.account.query.api.query.FindAccountByHolderQuery;
import com.techbank.account.query.api.query.FindAccountByIdQuery;
import com.techbank.account.query.api.query.FindAccountWithBalanceQuery;
import com.techbank.account.query.api.query.FindAllAccountsQuery;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.infrastructure.QueryDispatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/bankAccountLookup")
public class AccountLookupController {
    private final Logger logger = Logger.getLogger(AccountLookupController.class.getName());

    private final QueryDispatcher queryDispatcher;

    public AccountLookupController(QueryDispatcher queryDispatcher) {
        this.queryDispatcher = queryDispatcher;
    }

    @GetMapping(path = "/")
    public ResponseEntity<AccountLookupResponse> getAllAccounts() {
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAllAccountsQuery());
            return getAccountLookupResponseResponseEntity(accounts);
        } catch (Exception e) {
            String safeErrorMessage = "Failed to complete get all accounts request";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AccountLookupResponse(safeErrorMessage));
        }
    }

    @GetMapping(path = "/byId/{id}")
    public ResponseEntity<AccountLookupResponse> getAccountById(@PathVariable String id) {
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByIdQuery(id));
            return getAccountLookupResponseResponseEntity(accounts);
        } catch (Exception e) {
            String safeErrorMessage = "Failed to complete get account by Id request";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AccountLookupResponse(safeErrorMessage));
        }
    }

    @GetMapping(path = "/byHolder/{accountHolder}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(@PathVariable String accountHolder) {
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountByHolderQuery(accountHolder));
            return getAccountLookupResponseResponseEntity(accounts);
        } catch (Exception e) {
            String safeErrorMessage = "Failed to complete get account by holder request";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AccountLookupResponse(safeErrorMessage));
        }
    }

    @GetMapping(path = "/withBalance/{equalityType}/{balance}")
    public ResponseEntity<AccountLookupResponse> getAccountByHolder(@PathVariable EqualityType equalityType, @PathVariable double balance) {
        try {
            List<BankAccount> accounts = queryDispatcher.send(new FindAccountWithBalanceQuery(equalityType, balance));
            return getAccountLookupResponseResponseEntity(accounts);
        } catch (Exception e) {
            String safeErrorMessage = "Failed to complete get account with balance request";
            logger.log(Level.SEVERE, safeErrorMessage, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AccountLookupResponse(safeErrorMessage));
        }
    }

    private ResponseEntity<AccountLookupResponse> getAccountLookupResponseResponseEntity(List<BankAccount> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
        AccountLookupResponse response = AccountLookupResponse.builder()
                .accounts(accounts)
                .message(MessageFormat.format("Successfully returned {0} bank account(s)!", accounts.size()))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
