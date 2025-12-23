package com.ozkayret.banking.controller;

import com.ozkayret.banking.dtos.AccountHistoryResponse;
import com.ozkayret.banking.dtos.TransactionStatusResponse;
import com.ozkayret.banking.dtos.TransferRequest;
import com.ozkayret.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public TransactionStatusResponse transfer(@RequestBody TransferRequest request, Principal principal) {
        return transactionService.transfer(request, principal);
    }

    @PostMapping("/account/{accountId}")
    public List<AccountHistoryResponse> history(@PathVariable UUID accountId, Principal principal) {
        return transactionService.history(accountId, principal);
    }
}
