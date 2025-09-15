package com.colacco.finance.DTO;

import com.colacco.finance.Models.Transaction;
import com.colacco.finance.Models.TransactionType;

import java.math.BigDecimal;

public record TransactionOutputDTO (Long id, BigDecimal value, TransactionType transactionType){
    public TransactionOutputDTO(Transaction transaction) {
        this(transaction.getId(), transaction.getValue(), transaction.getTransactionType());
    }
}