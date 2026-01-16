package com.ozkayret.banking.service;

import com.ozkayret.banking.dtos.AccountDetailResponse;
import com.ozkayret.banking.dtos.AccountNumberDto;
import com.ozkayret.banking.dtos.AccountRequest;
import com.ozkayret.banking.dtos.AccountResponse;
import com.ozkayret.banking.entity.Account;
import com.ozkayret.banking.entity.User;
import com.ozkayret.banking.exception.AccountNotFoundException;
import com.ozkayret.banking.mapper.AccountMapper;
import com.ozkayret.banking.repository.AccountRepository;
import com.ozkayret.banking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private AccountMapper accountMapper;
    private AccountServiceImpl accountService;
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        userRepository = mock(UserRepository.class);
        accountMapper = mock(AccountMapper.class);
        accountService = new AccountServiceImpl(accountRepository, userRepository, accountMapper,messageSource);
    }

    @Test
    void createAccount() {
        AccountRequest request = mock(AccountRequest.class);
        Principal principal = () -> "user";
        User user = new User();
        user.setAccounts(new ArrayList<>());
        Account account = new Account();

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        when(accountMapper.toAccount(request)).thenReturn(account);

        accountService.createAccount(request, principal);

        verify(accountRepository).save(account);
        verify(userRepository).save(user);
        assertThat(user.getAccounts().contains(account));

    }

    @Test
    void createAccount2() {
        AccountRequest request = mock(AccountRequest.class);
        Principal principal = () -> "user";
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        assertThrows(AccountNotFoundException.class, () -> accountService.createAccount(request, principal));

    }

    @Test
    void getAccounts() {
        Account account = new Account();
        when(accountRepository.findAll()).thenReturn(List.of(account));
        List<AccountResponse> responses = List.of(mock(AccountResponse.class));
        when(accountMapper.toAccountResponseList(List.of(account))).thenReturn(responses);

       /* Page<AccountResponse> result = accountService.getAccounts(null, null);
        assertThat(responses).isEqualTo(result);
*/
        when(accountRepository.findByNumber("123")).thenReturn(Optional.of(account));
        when(accountMapper.toAccountResponseList(List.of(account))).thenReturn(responses);

       /* Page<AccountResponse> result2 = accountService.getAccounts(null, "123", new Pageable() {
        });
        assertThat(responses).isEqualTo(result2);*/

        when(accountRepository.findByName("vadesiz")).thenReturn(List.of(account));
        when(accountMapper.toAccountResponseList(List.of(account))).thenReturn(responses);

      /*  List<AccountResponse> result3 = accountService.getAccounts("vadesiz", null);
        assertThat(responses).isEqualTo(result3);*/
    }

    @Test
    void updateAccount() {
        AccountRequest request = mock(AccountRequest.class);
        when(request.number()).thenReturn("hesapno");
        when(request.balance()).thenReturn(new BigDecimal("100"));
        when(request.name()).thenReturn("vadesiz");

        Account account = new Account();
        when(accountRepository.findByNumber("hesapno")).thenReturn(Optional.of(account));

        accountService.updateAccount(request);

        assertThat(new BigDecimal("100")).isEqualTo(account.getBalance());
        assertThat("vadesiz").isEqualTo(account.getName());
        verify(accountRepository).save(account);

        when(request.number()).thenReturn("bos");
        when(accountRepository.findByNumber("bos")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount(request));
    }


    @Test
    void deleteAccount() {
        UUID id = UUID.randomUUID();
        accountService.deleteAccount(id);
        verify(accountRepository).deleteById(id);
    }

    @Test
    void getAccountById() {
        UUID id = UUID.randomUUID();
        Principal principal = () -> "user";
        User user = new User();
        user.setUsername("user");
        Account account = new Account();
        account.setUser(user);

        when(accountRepository.findById(id)).thenReturn(Optional.of(account));
        AccountDetailResponse response = mock(AccountDetailResponse.class);
        when(accountMapper.toAccountDetailResponse(account)).thenReturn(response);

        AccountDetailResponse result = accountService.getAccountById(id, principal);
        assertThat(response).isEqualTo(result);
    }

    @Test
    void generateAccountNumber() {
        AccountNumberDto dto = accountService.generateAccountNumber();
        assertThat(dto).isNotNull();
        assertThat(dto.accountNumber()).isNotNull();
        assertThat(dto.accountNumber().isEmpty()).isFalse();
    }
}