package com.ozkayret.banking.controller;

import com.ozkayret.banking.dtos.AccountDetailResponse;
import com.ozkayret.banking.dtos.AccountNumberDto;
import com.ozkayret.banking.dtos.AccountRequest;
import com.ozkayret.banking.dtos.AccountResponse;
import com.ozkayret.banking.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    /*
        Account Operations
    1. Create Account - POST /api/accounts - Creates a new account for the authenticated user.
    */
    @PostMapping
    public void createAccount(@RequestBody @Valid AccountRequest request, Principal principal) {
        accountService.createAccount(request, principal);
    }

    /*
2. Search Accounts - GET /api/accounts - Search accounts for the authenticated user. - Accounts should be filterable on number and name.
*/
    @GetMapping
    public List<AccountResponse> getAccounts(@RequestParam(value = "name", required = false) String name, @RequestParam(value = "number", required = false) String number) {
        return accountService.getAccounts(name, number);
    }
    /*
3. Update Account - PUT /api/accounts/{id} - Updates the selected account for the authenticated user.
*/

    @PutMapping
    public void updateAccount(@RequestBody @Valid AccountRequest request) {
        accountService.updateAccount(request);
    }

    /*
    4. Delete Account - DELETE /api/accounts/{id} - Deletes the selected account for the authenticated user.
    */
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
    }

    /*
    5. View Account Details - GET /api/accounts/{id} - Retrieves details of a specific account, including the balance. Access is restricted to the account owner.*/
    @GetMapping({"/{id}"})
    public AccountDetailResponse getAccountById(@PathVariable UUID id, Principal principal) {
        return accountService.getAccountById(id, principal);
    }

    @GetMapping({"/generate-account-number"})
    public AccountNumberDto generateAccountNumber() {
        return accountService.generateAccountNumber();
    }

}
