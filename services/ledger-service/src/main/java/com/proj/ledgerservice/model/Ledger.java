package com.proj.ledgerservice.model;

import com.proj.ledgerservice.enums.EntryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ledger_entries")
public class Ledger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long transactionId; // from transaction-service
    private Long accountId;

    private EntryType entryType;
    private BigDecimal amount;
    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();
}

