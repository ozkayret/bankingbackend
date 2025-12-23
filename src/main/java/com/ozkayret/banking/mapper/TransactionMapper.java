package com.ozkayret.banking.mapper;

import com.ozkayret.banking.dtos.AccountHistoryResponse;
import com.ozkayret.banking.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionMapper {
    public AccountHistoryResponse toAccountHistoryResponse(Transaction transaction) {
        return new AccountHistoryResponse(transaction.getId(), transaction.getFrom().getNumber(), transaction.getTo().getNumber(), transaction.getAmount(), transaction.getTransactionDate(), transaction.getStatus().toString());
    }

    public List<AccountHistoryResponse> toAccountHistoryResponse(List<Transaction> transactionList) {
        return transactionList.stream().map(this::toAccountHistoryResponse).toList();
    }
}
