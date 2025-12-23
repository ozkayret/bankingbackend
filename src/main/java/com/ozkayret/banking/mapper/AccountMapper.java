package com.ozkayret.banking.mapper;

import com.ozkayret.banking.dtos.AccountDetailResponse;
import com.ozkayret.banking.dtos.AccountHistoryResponse;
import com.ozkayret.banking.dtos.AccountRequest;
import com.ozkayret.banking.dtos.AccountResponse;
import com.ozkayret.banking.entity.Account;
import com.ozkayret.banking.entity.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Component
public class AccountMapper {
    public Account toAccount(AccountRequest request) {
        return Account.builder()
                .number(request.number())
                .name(request.name())
                .balance(request.balance())
                .build();
    }

    public AccountResponse toAccountResponse(Account account) {
        return new AccountResponse(account.getId(),account.getNumber(), account.getName(), account.getBalance());
    }

    public List<AccountResponse> toAccountResponseList(List<Account> accounts) {
        return accounts.stream().map(this::toAccountResponse).toList();
    }

    public AccountDetailResponse toAccountDetailResponse(Account account) {
        return new AccountDetailResponse(account.getId(),account.getNumber(), account.getName(), account.getBalance());
    }

}
