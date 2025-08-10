package com.proj.customerservice.service;

import com.proj.customerservice.model.Customer;
import com.proj.customerservice.repository.CustomerRepository;
import com.proj.customerservice.request.CreateCustomerReq;
import com.proj.customerservice.request.UpdateCustomerReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    
    @Override
    public Customer createCustomer(CreateCustomerReq request) {
        Customer customer = new Customer();
        customer.setUserId(request.userId());
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setPhoneNumber(request.phoneNumber());
        customer.setAddress(request.address());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        
        return customerRepository.save(customer);
    }
    
    @Override
    public Customer getCustomer(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }
    
    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).orElse(null);
    }
    
    @Override
    public Customer updateCustomer(Long customerId, UpdateCustomerReq request) {
        Customer existingCustomer = customerRepository.findById(customerId).orElse(null);
        if (existingCustomer != null) {
            if (request.firstName() != null) {
                existingCustomer.setFirstName(request.firstName());
            }
            if (request.lastName() != null) {
                existingCustomer.setLastName(request.lastName());
            }
//            if (request.email() != null) {
//                existingCustomer.setEmail(request.email());
//            }
            if (request.phoneNumber() != null) {
                existingCustomer.setPhoneNumber(request.phoneNumber());
            }
            if (request.address() != null) {
                existingCustomer.setAddress(request.address());
            }
            existingCustomer.setUpdatedAt(LocalDateTime.now());
            
            return customerRepository.save(existingCustomer);
        }
        return null;
    }
    
    @Override
    public boolean deleteCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer != null) {
            customerRepository.delete(customer);
            return true;
        }
        return false;
    }
} 