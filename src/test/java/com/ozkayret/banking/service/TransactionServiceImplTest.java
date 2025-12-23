package com.ozkayret.banking.service;

import com.ozkayret.banking.dtos.AccountHistoryResponse;
import com.ozkayret.banking.dtos.TransactionStatusResponse;
import com.ozkayret.banking.dtos.TransferRequest;
import com.ozkayret.banking.entity.Account;
import com.ozkayret.banking.entity.Transaction;
import com.ozkayret.banking.entity.User;
import com.ozkayret.banking.enums.Status;
import com.ozkayret.banking.mapper.TransactionMapper;
import com.ozkayret.banking.repository.AccountRepository;
import com.ozkayret.banking.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private TransactionMapper transactionMapper;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionRepository = mock(TransactionRepository.class);
        accountRepository = mock(AccountRepository.class);
        transactionMapper = mock(TransactionMapper.class);
        transactionService = new TransactionServiceImpl(transactionRepository, accountRepository, transactionMapper);
    }
    @Test
    void transfer() {
        TransferRequest request = new TransferRequest("from", "to", BigDecimal.valueOf(50));
        Principal principal = () -> "user";
        Account fromAccount = new Account();
        fromAccount.setBalance(BigDecimal.valueOf(100));
        User user = new User();
        user.setUsername("user");
        fromAccount.setUser(user);
        fromAccount.setSentTransactions(new ArrayList<>());
        Account toAccount = new Account();
        toAccount.setBalance(BigDecimal.valueOf(50));
        toAccount.setSentTransactions(new ArrayList<>());
        when(accountRepository.findByNumber("from")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByNumber("to")).thenReturn(Optional.of(toAccount));

        TransactionStatusResponse response = transactionService.transfer(request, principal);

        assertThat(response.status()).isEqualTo(Status.SUCCESS.toString());
        assertThat(fromAccount.getBalance()).isEqualTo(BigDecimal.valueOf(50));
        assertThat(toAccount.getBalance()).isEqualTo(BigDecimal.valueOf(100));
        verify(transactionRepository).save(any(Transaction.class));
        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
    }

    @Test
    void history() {
        UUID accountId = UUID.randomUUID();
        Principal principal = () -> "user";
        Account account = new Account();
        User user = new User();
        user.setUsername("user");
        account.setUser(user);
        List<Transaction> transactions = new ArrayList<>();
        account.setSentTransactions(transactions);
        List<AccountHistoryResponse> expectedHistory = List.of(mock(AccountHistoryResponse.class));
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionMapper.toAccountHistoryResponse(transactions)).thenReturn(expectedHistory);

        List<AccountHistoryResponse> result = transactionService.history(accountId, principal);
        assertThat(result).isEqualTo(expectedHistory);
    }
}