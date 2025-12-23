package com.ozkayret.banking.service;

import com.ozkayret.banking.dtos.AccountDetailResponse;
import com.ozkayret.banking.dtos.AccountNumberDto;
import com.ozkayret.banking.dtos.AccountRequest;
import com.ozkayret.banking.dtos.AccountResponse;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface AccountService {
    void createAccount(AccountRequest request,Principal principal);

    List<AccountResponse> getAccounts(String name,String accountNumber);

    void updateAccount(AccountRequest request);

    void deleteAccount(UUID id);

    AccountDetailResponse getAccountById(UUID id, Principal principal);

    AccountNumberDto generateAccountNumber();
}
