package com.proj.accountservice.controller;

import com.proj.accountservice.model.Account;
import com.proj.accountservice.request.CreateAccountReq;
import com.proj.accountservice.response.ApiResponse;
import com.proj.accountservice.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse> createAccount(@Valid @RequestBody CreateAccountReq request) {
        try {
            Account account = accountService.createAccount(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Account Created!", account));
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse> getAccount(@PathVariable Long accountId) {
        try {
            Account account = accountService.getAccount(accountId);
            if (account != null) {
                return ResponseEntity.ok(new ApiResponse("Account retrieved successfully", account));
            } else {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Account not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error retrieving account: " + e.getMessage(), null));
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse> getAccountsByCustomerId(@PathVariable Long customerId) {
        try {
            List<Account> accounts = accountService.getAccountsByCustomerId(customerId);
            if (accounts != null && !accounts.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse("Accounts retrieved successfully", accounts));
            } else {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No accounts found for customer", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error retrieving accounts: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<ApiResponse> updateAccount(@PathVariable Long accountId, @Valid @RequestBody CreateAccountReq request) {
        try {
            Account updatedAccount = accountService.updateAccount(accountId, request);
            if (updatedAccount != null) {
                return ResponseEntity.ok(new ApiResponse("Account updated successfully", updatedAccount));
            } else {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Account not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating account: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<ApiResponse> deleteAccount(@PathVariable Long accountId) {
        try {
            boolean deleted = accountService.deleteAccount(accountId);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse("Account deleted successfully", null));
            } else {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Account not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error deleting account: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<ApiResponse> getAccountBalance(@PathVariable Long accountId) {
        try {
            java.math.BigDecimal balance = accountService.getAccountBalance(accountId);
            if (balance != null) {
                return ResponseEntity.ok(new ApiResponse("Account balance retrieved successfully", balance));
            } else {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Account not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error retrieving account balance: " + e.getMessage(), null));
        }
    }
} 