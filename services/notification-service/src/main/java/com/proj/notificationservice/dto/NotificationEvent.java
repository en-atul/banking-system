package com.proj.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private String eventType; // ACCOUNT_CREATED, ACCOUNT_UPDATED
    private Long userId;
    private Long accountId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private LocalDateTime timestamp;
    private String message;
} 