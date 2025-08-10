package com.proj.customerservice.controller;

import com.proj.customerservice.model.Customer;
import com.proj.customerservice.request.CreateCustomerReq;
import com.proj.customerservice.request.UpdateCustomerReq;
import com.proj.customerservice.response.ApiResponse;
import com.proj.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse> createCustomer(@Valid @RequestBody CreateCustomerReq request) {
        try {
            Customer customer = customerService.createCustomer(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Customer created successfully", customer));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error creating customer: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse> getCustomer(@PathVariable Long customerId) {
        try {
            Customer customer = customerService.getCustomer(customerId);
            if (customer != null) {
                return ResponseEntity.ok(new ApiResponse("Customer retrieved successfully", customer));
            } else {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Customer not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error retrieving customer: " + e.getMessage(), null));
        }
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse> getCustomerByEmail(@PathVariable String email) {
        try {
            Customer customer = customerService.getCustomerByEmail(email);
            if (customer != null) {
                return ResponseEntity.ok(new ApiResponse("Customer retrieved successfully", customer));
            } else {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Customer not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error retrieving customer: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse> updateCustomer(@PathVariable Long customerId, @Valid @RequestBody UpdateCustomerReq request) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(customerId, request);
            if (updatedCustomer != null) {
                return ResponseEntity.ok(new ApiResponse("Customer updated successfully", updatedCustomer));
            } else {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Customer not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating customer: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<ApiResponse> deleteCustomer(@PathVariable Long customerId) {
        try {
            boolean deleted = customerService.deleteCustomer(customerId);
            if (deleted) {
                return ResponseEntity.ok(new ApiResponse("Customer deleted successfully", null));
            } else {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Customer not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error deleting customer: " + e.getMessage(), null));
        }
    }
}
