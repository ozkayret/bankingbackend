package com.ozkayret.banking.service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.ozkayret.banking.dtos.AccountHistoryResponse;
import com.ozkayret.banking.dtos.TransactionStatusResponse;
import com.ozkayret.banking.dtos.TransferRequest;
import com.ozkayret.banking.entity.Account;
import com.ozkayret.banking.entity.Transaction;
import com.ozkayret.banking.enums.Status;
import com.ozkayret.banking.exception.AccountNotFoundException;
import com.ozkayret.banking.exception.AmountNotBeNegative;
import com.ozkayret.banking.exception.ForbiddenException;
import com.ozkayret.banking.mapper.TransactionMapper;
import com.ozkayret.banking.repository.AccountRepository;
import com.ozkayret.banking.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public TransactionStatusResponse transfer(TransferRequest request, Principal principal) {
        String status=Status.SUCCESS.toString();

        if(request.amount().compareTo(BigDecimal.ZERO) <= 0){
            throw new AmountNotBeNegative(
                    messageSource.getMessage("amount.not.negative", null, LocaleContextHolder.getLocale()));
        }
        Account accountFrom = accountRepository.findByNumber(request.from()).orElseThrow(
                () -> new AccountNotFoundException(messageSource.getMessage("account.not.found.with.number",
                        new Object[] { request.from() }, LocaleContextHolder.getLocale())));
        if (!accountFrom.getUser().getUsername().equals(principal.getName())) {
            log.error(messageSource.getMessage("account.access.denied", null, LocaleContextHolder.getLocale()));
            throw new ForbiddenException(
                    messageSource.getMessage("account.access.denied", null, LocaleContextHolder.getLocale()));
        }
        Account accountTo = accountRepository.findByNumber(request.to()).orElseThrow(
                () -> new AccountNotFoundException(messageSource.getMessage("account.not.found.with.number",
                        new Object[] { request.to() }, LocaleContextHolder.getLocale())));
        Transaction transaction = new Transaction();
        transaction.setTo(accountTo);
        transaction.setFrom(accountFrom);
        transaction.setAmount(request.amount());
        transaction.setTransactionDate(LocalDateTime.now());
        if (request.amount().compareTo(accountFrom.getBalance()) > 0) {
            transaction.setStatus(Status.FAILED);
            status=Status.FAILED.toString();
            /*throw new AccountBalanceNotEnough(
                    "Hesapta yeterli bakiye yok");*/
        }else{
            transaction.setStatus(Status.SUCCESS);
            accountFrom.setBalance(accountFrom.getBalance().subtract(request.amount()));
            accountTo.setBalance(accountTo.getBalance().add(request.amount()));
            accountFrom.getSentTransactions().add(transaction);
            accountTo.getSentTransactions().add(transaction);
        }
        transactionRepository.save(transaction);
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
        return new TransactionStatusResponse(status);
    }

    @Override
    public List<AccountHistoryResponse> history(UUID accountId,Principal principal) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(messageSource.getMessage("account.not.found.with.id",
                        new Object[] { accountId }, LocaleContextHolder.getLocale())));
        if (!account.getUser().getUsername().equals(principal.getName())) {
            log.error(messageSource.getMessage("account.access.denied", null, LocaleContextHolder.getLocale()));
            throw new ForbiddenException(
                    messageSource.getMessage("account.access.denied", null, LocaleContextHolder.getLocale()));
        }
        return transactionMapper.toAccountHistoryResponse(account.getSentTransactions());
    }
}
