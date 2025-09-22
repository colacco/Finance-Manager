package com.colacco.finance.dto;

import com.colacco.finance.models.Transaction;
import com.colacco.finance.models.TransactionType;

import java.math.BigDecimal;

public record TransactionOutputDTO (Long id, BigDecimal value, TransactionType transactionType){
    public TransactionOutputDTO(Transaction transaction) {
        this(transaction.getId(), transaction.getValue(), transaction.getTransactionType());
    }
}