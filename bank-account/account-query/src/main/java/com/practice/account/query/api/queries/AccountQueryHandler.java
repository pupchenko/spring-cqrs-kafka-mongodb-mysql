package com.practice.account.query.api.queries;

import com.practice.account.query.api.dto.EqualityType;
import com.practice.account.query.domain.AccountRepository;
import com.practice.account.query.domain.BankAccount;
import com.practice.cqrs.core.domain.BaseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AccountQueryHandler implements QueryHandler{
    private AccountRepository accountRepository;

    public AccountQueryHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public List<BaseEntity> handle(FindAllAccountsQuery query) {
        Iterable<BankAccount> bankAccounts = accountRepository.findAll();
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccounts.forEach(bankAccountList::add);
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountByIdQuery query) {
        var bankAccount = accountRepository.findById(query.getId());
        if (bankAccount.isEmpty()) {
            return Collections.emptyList();
        }
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountHolderQuery query) {
        var bankAccount = accountRepository.findByAccountHolder(query.getAccountHolder());
        if (bankAccount.isEmpty()) {
            return Collections.emptyList();
        }
        List<BaseEntity> bankAccountList = new ArrayList<>();
        bankAccountList.add(bankAccount.get());
        return bankAccountList;
    }

    @Override
    public List<BaseEntity> handle(FindAccountWithBalanceQuery query) {
        List<BaseEntity> bankAccountList = query.getEqualityType() == EqualityType.GREATER_THAN
                ? accountRepository.findByBalanceGreaterThan(query.getBalance())
                : accountRepository.findByBalanceLessThan(query.getBalance());
        return bankAccountList;
    }
}
