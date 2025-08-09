package com.proj.accountservice.service;

import com.proj.accountservice.model.Account;
import com.proj.accountservice.repository.AccountRepository;
import com.proj.accountservice.request.CreateAccountReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final NotificationProducerService notificationProducerService;

    @Override
    public Account createAccount(CreateAccountReq request) {
        Account account = new Account();
        account.setCustomerId(request.customerId());
        account.setAccountType(request.accountType());
        account.setAccountNumber(generateAccountNumber());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        if (request.balance() != null) {
            account.setBalance(request.balance());
        }

        Account savedAccount = accountRepository.save(account);
        
        // Send notification asynchronously
        notificationProducerService.sendAccountCreatedNotification(
            request.customerId(), 
            savedAccount.getId(), 
            savedAccount.getAccountNumber(), 
            savedAccount.getAccountType().toString(), 
            savedAccount.getBalance()
        );
        
        return savedAccount;
    }

    @Override
    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }

    @Override
    public List<Account> getAccountsByCustomerId(Long customerId){
        return accountRepository.findAllByCustomerId(customerId).orElse(null);
    }

    @Override
    public Account updateAccount(Long accountId, CreateAccountReq request) {
        Account existingAccount = accountRepository.findById(accountId).orElse(null);
        if (existingAccount != null) {
            existingAccount.setAccountType(request.accountType());
            if (request.balance() != null) {
                existingAccount.setBalance(request.balance());
            }
            existingAccount.setUpdatedAt(LocalDateTime.now());
            
            Account updatedAccount = accountRepository.save(existingAccount);
            
            // Send notification asynchronously
            notificationProducerService.sendAccountUpdatedNotification(
                existingAccount.getCustomerId(), 
                updatedAccount.getId(), 
                updatedAccount.getAccountNumber(), 
                updatedAccount.getAccountType().toString(), 
                updatedAccount.getBalance()
            );
            
            return updatedAccount;
        }
        return null;
    }

    @Override
    public boolean deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account != null) {
            accountRepository.delete(account);
            return true;
        }
        return false;
    }

    @Override
    public BigDecimal getAccountBalance(Long accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        return account != null ? account.getBalance() : null;
    }

    public static String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        // Example: 12-digit account number
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
