package com.proj.customerservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/customers")
public class CustomerController {

    @PostMapping
    public void createCustomer(){
    }

    @GetMapping("/{customerId}")
    public void getCustomer(@PathVariable Long customerId){
    }

    @GetMapping("/email/{email}")
    public void getCustomerByEmail(@PathVariable Long email){
    }

    @PutMapping("/{customerId}")
    public void updateCustomer(@PathVariable Long customerId){
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable Long customerId){
    }
}
