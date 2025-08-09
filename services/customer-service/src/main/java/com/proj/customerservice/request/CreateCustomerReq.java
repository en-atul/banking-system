package com.proj.customerservice.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateCustomerReq(
    @NotNull(message = "User ID is required")
    Long userId,
    
    @NotBlank(message = "First name is required")
    String firstName,
    
    @NotBlank(message = "Last name is required")
    String lastName,
    
//    @NotBlank(message = "Email is required")
//    @Email(message = "Invalid email format")
//    String email,
    
    String phoneNumber,
    String address
) {}
