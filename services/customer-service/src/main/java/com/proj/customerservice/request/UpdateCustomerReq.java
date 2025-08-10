package com.proj.customerservice.request;

//import jakarta.validation.constraints.Email;

public record UpdateCustomerReq(
    String firstName,
    String lastName,
//    @Email(message = "Invalid email format")
//    String email,
    String phoneNumber,
    String address
) {} 