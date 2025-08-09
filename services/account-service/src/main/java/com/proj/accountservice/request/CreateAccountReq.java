package com.proj.accountservice.request;


import com.proj.accountservice.enums.AccountType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountReq(
        @NotEmpty Long customerId,
        @NotEmpty AccountType accountType,
        @NotNull BigDecimal balance
        ) {
}
