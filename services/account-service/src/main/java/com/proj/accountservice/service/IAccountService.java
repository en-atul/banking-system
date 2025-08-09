package com.proj.accountservice.service;

import com.proj.accountservice.model.Account;
import com.proj.accountservice.request.CreateAccountReq;

import java.math.BigDecimal;
import java.util.List;

public interface IAccountService {
    Account createAccount(CreateAccountReq request);

    Account getAccount(Long accountId);

    List<Account> getAccountsByCustomerId(Long customerId);
    
    Account updateAccount(Long accountId, CreateAccountReq request);
    
    boolean deleteAccount(Long accountId);
    
    BigDecimal getAccountBalance(Long accountId);
}
