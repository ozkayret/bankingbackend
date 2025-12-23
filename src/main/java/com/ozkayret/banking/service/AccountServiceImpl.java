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

    @Override
    @Transactional
    public void createAccount(AccountRequest request, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new AccountNotFoundException("User not found"));
        Account account = accountMapper.toAccount(request);
        account.setUser(user);
        accountRepository.save(account);
        user.getAccounts().add(account);
        userRepository.save(user);
    }

    @Override
    public List<AccountResponse> getAccounts(String name, String id) {
        if (id != null && !id.equals("")) {
            Account accounts = accountRepository.findByNumber(id).orElseThrow(() -> new AccountNotFoundException("Hesap bulunamadı"));
            return accountMapper.toAccountResponseList(List.of(accounts));
        } else if (name != null && !name.equals("")) {
            List<Account> accounts = accountRepository.findByName(name);
            return accountMapper.toAccountResponseList(accounts);
        } else {
            List<Account> accounts = accountRepository.findAll();
            return accountMapper.toAccountResponseList(accounts);
        }
    }


    @Override
    public void updateAccount(AccountRequest request) {
        Account account = accountRepository.findByNumber(request.number()).orElseThrow(() -> new AccountNotFoundException(String.format("%s numaralı hesap bulunmadı", request.number())));
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
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(String.format("%s id'li hesap bulunmadı", id)));

        if (!account.getUser().getUsername().equals(principal.getName())) {
            log.error(String.format("Hesap sahibi olmadığınız için hesap bilgilerine erişemezsiniz", id));
            throw new ForbiddenException(
                    String.format("Hesap sahibi olmadığınız için hesap bilgilerine erişemezsiniz", id));
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
