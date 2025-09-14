package com.colacco.finance.Models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Transaction{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public Transaction(BigDecimal value, TransactionType transactionType){
        this.value = value;
        this.transactionType = transactionType;
    }
}