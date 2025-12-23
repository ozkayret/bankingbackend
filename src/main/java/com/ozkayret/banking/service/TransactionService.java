package com.ozkayret.banking.service;

import com.ozkayret.banking.dtos.AccountHistoryResponse;
import com.ozkayret.banking.dtos.TransactionStatusResponse;
import com.ozkayret.banking.dtos.TransferRequest;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionStatusResponse transfer(TransferRequest request, Principal principal);

    List<AccountHistoryResponse> history(UUID accountId, Principal principal);
}
