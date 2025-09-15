package com.colacco.finance.DTO;

import com.colacco.finance.Models.Transaction;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal value) {
    public TransactionDTO(Transaction transaction) {
        this(transaction.getValue());
    }
}