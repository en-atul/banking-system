package com.proj.accountservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @PostMapping
    public String createAccount(@RequestHeader("X-User-ID") String userId,
                              @RequestHeader("X-Username") String username) {
        
        // No JWT validation needed here - API Gateway already did it
        return String.format("Creating account for user %s (ID: %s)", username, userId);
    }

    @GetMapping("/{accountId}")
    public String getAccount(@PathVariable String accountId,
                             @RequestHeader("X-User-ID") String userId,
                             @RequestHeader("X-Username") String username,
                             @RequestHeader("X-User-Roles") String userRoles) {

        // The API Gateway has already validated the JWT token
        // We just use the user information from headers
        return String.format("Account %s for user %s (ID: %s, Roles: %s)",
                accountId, username, userId, userRoles);
    }

    @GetMapping("/customer/{customerId}")
    public void getAllAccounts(@PathVariable String customerId) {

    }

    @PutMapping("/{accountId}")
    public void updateAccount(@PathVariable Long accountId){
    }

    @DeleteMapping("/{accountId}")
    public void deleteAccount(@PathVariable Long accountId){
    }

    @GetMapping("/{accountId}/balance")
    public void getAccountBalance(@PathVariable Long accountId){
    }

} 