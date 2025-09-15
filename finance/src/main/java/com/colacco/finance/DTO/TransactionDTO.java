package com.colacco.finance.DTO;

import com.colacco.finance.Models.Transaction;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionDTO(@NotNull BigDecimal value) {
    public TransactionDTO(Transaction transaction) {
        this(transaction.getValue());
    }
}