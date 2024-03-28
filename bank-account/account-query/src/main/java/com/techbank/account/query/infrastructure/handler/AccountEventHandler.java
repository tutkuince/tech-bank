package com.techbank.account.query.infrastructure.handler;

import com.techbank.account.common.event.AccountClosedEvent;
import com.techbank.account.common.event.AccountOpenedEvent;
import com.techbank.account.common.event.FundsDepositedEvent;
import com.techbank.account.common.event.FundsWithdrawnEvent;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.cqrs.core.event.BaseEvent;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AccountEventHandler implements EventHandler {

    private final AccountRepository accountRepository;

    public AccountEventHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void on(AccountOpenedEvent event) {
        BankAccount bankAccount = BankAccount.builder()
                .id(event.getId())
                .accountHolder(event.getAccountHolder())
                .creationDate(LocalDateTime.now())
                .accountType(event.getAccountType())
                .balance(event.getOpeningBalance())
                .build();
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsDepositedEvent event) {
        BankAccount bankAccount = getBankAccount(event);
        double currentBalance = bankAccount.getBalance();
        double latestBalance = currentBalance + event.getAmount();
        bankAccount.setBalance(latestBalance);
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(FundsWithdrawnEvent event) {
        BankAccount bankAccount = getBankAccount(event);
        double currentBalance = bankAccount.getBalance();
        double latestBalance = currentBalance - event.getAmount();
        bankAccount.setBalance(latestBalance);
        accountRepository.save(bankAccount);
    }

    @Override
    public void on(AccountClosedEvent event) {
        accountRepository.deleteById(event.getId());
    }

    private BankAccount getBankAccount(BaseEvent event) {
        Optional<BankAccount> bankAccount = accountRepository.findById(event.getId());
        if (bankAccount.isEmpty()) {
            throw new RuntimeException("Bank Account cannot be found!");
        }
        return bankAccount.get();
    }
}
