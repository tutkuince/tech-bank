package com.techbank.account.query.api.query;

import com.techbank.account.query.api.dto.EqualityType;
import com.techbank.account.query.domain.AccountRepository;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.cqrs.core.domain.BaseEntity;
import com.techbank.cqrs.core.event.BaseEvent;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountQueryHandler implements QueryHandler {

    private final AccountRepository accountRepository;

    public AccountQueryHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        Iterable<BankAccount> bankAccounts = accountRepository.findAll();
        List<BaseEntity> bankAccountsList = new ArrayList<>();
        bankAccounts.forEach(bankAccountsList::add);
        return bankAccountsList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountWithBalanceQuery query) {
        return query.getEqualityType() == EqualityType.GREATER_THAN ?
                accountRepository.findByBalanceGreaterThan(query.getBalance()) :
                accountRepository.findByBalanceLessThan(query.getBalance());
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {
        Optional<BankAccount> bankAccount = accountRepository.findById(query.getId());
        if (bankAccount.isEmpty()) {
            return null;
        }
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByHolderQuery query) {
        Optional<BankAccount> byAccountHolder = accountRepository.findByAccountHolder(query.getAccountHolder());
        if (byAccountHolder.isEmpty()) {
            return null;
        }
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(byAccountHolder.get());
        return bankAccountList;
    }
}
