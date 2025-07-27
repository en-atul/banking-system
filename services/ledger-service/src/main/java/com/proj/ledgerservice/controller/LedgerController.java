package com.proj.ledgerservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/ledger-entries")
public class LedgerController {

    @PostMapping
    public void createEntry(){
    }

    @GetMapping("/{entryId}")
    public void getEntry(@PathVariable Long entryId){
    }

    @GetMapping("/account/{accountId}")
    public void getAllEntriesForAccount(@PathVariable Long accountId){
    }

    // Used to retrieve all ledger entries that were
    // created as part of a specific transaction.
    @PutMapping("/transaction/{transactionId}")
    public void getEntriesForTransaction(@PathVariable Long transactionId){
    }

}
