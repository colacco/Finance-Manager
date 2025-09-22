package com.colacco.finance.dto;

import com.colacco.finance.models.Transaction;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionDTO(@NotNull BigDecimal value) {
    public TransactionDTO(Transaction transaction) {
        this(transaction.getValue());
    }
}