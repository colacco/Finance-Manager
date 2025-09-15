package com.colacco.finance.Models;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "amount")
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    public Transaction(BigDecimal value, TransactionType transactionType, User user) {
        this.value = value;
        this.transactionType = transactionType;
        this.user = user;
    }
}