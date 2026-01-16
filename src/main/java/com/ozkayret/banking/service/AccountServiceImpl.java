package com.ozkayret.banking.service;

import com.ozkayret.banking.dtos.AccountDetailResponse;
import com.ozkayret.banking.dtos.AccountNumberDto;
import com.ozkayret.banking.dtos.AccountRequest;
import com.ozkayret.banking.dtos.AccountResponse;
import com.ozkayret.banking.entity.Account;
import com.ozkayret.banking.entity.User;
import com.ozkayret.banking.exception.AccountNotFoundException;
import com.ozkayret.banking.exception.ForbiddenException;
import com.ozkayret.banking.mapper.AccountMapper;
import com.ozkayret.banking.repository.AccountRepository;
import com.ozkayret.banking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.management.ManagementFactory;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public void createAccount(AccountRequest request, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new AccountNotFoundException(
                messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale())));
        Account account = accountMapper.toAccount(request);
        account.setUser(user);
        accountRepository.save(account);
        user.getAccounts().add(account);
        userRepository.save(user);
    }

    @Override
    public Page<AccountResponse> getAccounts(String name, String id, Pageable pageable) {
        if (id != null && !id.equals("")) {
            Account account = accountRepository.findByNumber(id).orElseThrow(() -> new AccountNotFoundException(
                    messageSource.getMessage("account.not.found", null, LocaleContextHolder.getLocale())));
            return new PageImpl<>(List.of(accountMapper.toAccountResponse(account)), pageable, 1);
        } else if (name != null && !name.equals("")) {
            Page<Account> accounts = accountRepository.findByName(name, pageable);
            return accounts.map(accountMapper::toAccountResponse);
        } else {
            Page<Account> accounts = accountRepository.findAll(pageable);
            return accounts.map(accountMapper::toAccountResponse);
        }
    }


    @Override
    public void updateAccount(AccountRequest request) {
        Account account = accountRepository.findByNumber(request.number()).orElseThrow(
                () -> new AccountNotFoundException(messageSource.getMessage("account.not.found.with.number",
                        new Object[]{request.number()}, LocaleContextHolder.getLocale())));
        account.setBalance(request.balance());
        account.setName(request.name());
        accountRepository.save(account);
    }

    @Override
    public void deleteAccount(UUID id) {
        accountRepository.deleteById(id);
    }

    @Override
    public AccountDetailResponse getAccountById(UUID id, Principal principal) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(messageSource
                .getMessage("account.not.found.with.id", new Object[]{id}, LocaleContextHolder.getLocale())));

        if (!account.getUser().getUsername().equals(principal.getName())) {
            log.error(messageSource.getMessage("account.access.denied", null, LocaleContextHolder.getLocale()));
            throw new ForbiddenException(
                    messageSource.getMessage("account.access.denied", null, LocaleContextHolder.getLocale()));
        }

        return accountMapper.toAccountDetailResponse(account);
    }

    @Override
    public AccountNumberDto generateAccountNumber() {
        return new AccountNumberDto(generateProcessIdBased());
    }

    private String getProcessId() {
        String pid = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.toString(Integer.parseInt(pid.split("@")[0]), 36);
    }

    private String getTimestamp() {
        long currentTime = System.currentTimeMillis();
        return Long.toString(currentTime, 36);
    }

    private String generateProcessIdBased() {
        String processId = getProcessId();
        String timestamp = getTimestamp();

        return processId + timestamp;
    }


}
