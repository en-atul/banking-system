package com.proj.customerservice.request;


public record UpdateCustomerReq(
    String firstName,
    String lastName,

    String phoneNumber,
    String address
) {} 