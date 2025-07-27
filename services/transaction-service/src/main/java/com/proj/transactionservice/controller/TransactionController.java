package com.proj.transactionservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @PostMapping("/transfer")
    public void transferFund(){
    }

    @GetMapping("/deposit")
    public void depositFund(){
    }

    @GetMapping("/withdraw")
    public void withdrawFund(){
    }

    @PutMapping("/{transactionId}")
    public void getTransaction(@PathVariable Long transactionId){
    }

    @DeleteMapping("/account/{accountId}")
    public void getAllTransactions(@PathVariable Long accountId){
    }
}
