package com.proj.accountservice.request;


import com.proj.accountservice.enums.AccountType;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountReq(
        @NotNull Long customerId,
        @NotNull AccountType accountType,
        @NotNull BigDecimal balance
        ) {
}
