package com.proj.customerservice.service;

import com.proj.customerservice.model.Customer;
import com.proj.customerservice.request.CreateCustomerReq;
import com.proj.customerservice.request.UpdateCustomerReq;

public interface CustomerService {
    
    Customer createCustomer(CreateCustomerReq request);
    
    Customer getCustomer(Long customerId);
    
    Customer getCustomerByEmail(String email);
    
    Customer updateCustomer(Long customerId, UpdateCustomerReq request);
    
    boolean deleteCustomer(Long customerId);
} 