package com.ozkayret.banking.controller;

import com.ozkayret.banking.dtos.AccountDetailResponse;
import com.ozkayret.banking.dtos.AccountNumberDto;
import com.ozkayret.banking.dtos.AccountRequest;
import com.ozkayret.banking.dtos.AccountResponse;
import com.ozkayret.banking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing user accounts")
public class AccountController {
    private final AccountService accountService;

    /*
        Account Operations
    1. Create Account - POST /api/accounts - Creates a new account for the authenticated user.
    */
    @Operation(summary = "Create a new account", description = "Creates a new account for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Account created successfully")
    @PostMapping
    public void createAccount(@RequestBody @Valid AccountRequest request, Principal principal) {
        accountService.createAccount(request, principal);
    }

    /*
2. Search Accounts - GET /api/accounts - Search accounts for the authenticated user. - Accounts should be filterable on number and name.
*/
    @Operation(summary = "Search accounts", description = "Search accounts for the authenticated user by name or number.")
    @ApiResponse(responseCode = "200", description = "List of accounts found", content = @Content(schema = @Schema(implementation = AccountResponse.class)))
    @GetMapping
    public Page<AccountResponse> getAccounts(@RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "number", required = false) String number, @RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return accountService.getAccounts(name, number, pageable);
    }
    /*
3. Update Account - PUT /api/accounts/{id} - Updates the selected account for the authenticated user.
*/

    @Operation(summary = "Update an account", description = "Updates the selected account for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Account updated successfully")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PutMapping
    public void updateAccount(@RequestBody @Valid AccountRequest request) {
        accountService.updateAccount(request);
    }

    /*
    4. Delete Account - DELETE /api/accounts/{id} - Deletes the selected account for the authenticated user.
    */
    @Operation(summary = "Delete an account", description = "Deletes the selected account for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Account deleted successfully")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
    }

    /*
    5. View Account Details - GET /api/accounts/{id} - Retrieves details of a specific account, including the balance. Access is restricted to the account owner.*/
    @Operation(summary = "Get account details", description = "Retrieves details of a specific account, including the balance. Access is restricted to the account owner.")
    @ApiResponse(responseCode = "200", description = "Account details retrieved successfully", content = @Content(schema = @Schema(implementation = AccountDetailResponse.class)))
    @ApiResponse(responseCode = "403", description = "Access denied")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping({"/{id}"})
    public AccountDetailResponse getAccountById(@PathVariable UUID id, Principal principal) {
        return accountService.getAccountById(id, principal);
    }

    @Operation(summary = "Generate account number", description = "Generates a new unique account number.")
    @ApiResponse(responseCode = "200", description = "Account number generated successfully", content = @Content(schema = @Schema(implementation = AccountNumberDto.class)))
    @GetMapping({"/generate-account-number"})
    public AccountNumberDto generateAccountNumber() {
        return accountService.generateAccountNumber();
    }

}
