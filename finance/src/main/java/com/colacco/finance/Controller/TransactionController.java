package com.colacco.finance.Controller;

import com.colacco.finance.DTO.TransactionDTO;
import com.colacco.finance.Models.Transaction;
import com.colacco.finance.Models.TransactionType;
import com.colacco.finance.Repository.TransactionRepository;
import com.colacco.finance.Repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/{id}")
public class TransactionController {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public TransactionRepository transactionRepository;

    private BigDecimal balance(Long id){
        BigDecimal total = BigDecimal.valueOf(0);
        List<Transaction> transactionList = transactionRepository.findAll().stream().filter(transaction -> transaction.getUser().equals(userRepository.getReferenceById(id))).toList();

        for (Transaction transaction : transactionList){
            if (transaction.getTransactionType().equals(TransactionType.INPUT)) {
                total = total.add(transaction.getValue());
            } else {
                total = total.subtract(transaction.getValue());
            }
        }

        return total;
    }

    @PostMapping("/{io}/launch")
    @Transactional
    public void launch(@RequestBody @Valid TransactionDTO transactionDTO, @PathVariable Long id, @PathVariable int io){
        if (io == 1){
            transactionRepository.save(new Transaction(transactionDTO.value(), TransactionType.INPUT, userRepository.getReferenceById(id)));
        } else {
            if (balance(id).compareTo(transactionDTO.value()) > 0){
                transactionRepository.save(new Transaction(transactionDTO.value(), TransactionType.OUTPUT, userRepository.getReferenceById(id)));
            } else {
                System.out.println("Saldo insuficiente");
            }
        }
    }

    @GetMapping("/list")
    public Page<TransactionDTO> list(@PathVariable Long id, Pageable pageable){
        return transactionRepository.findById(id).stream().map(TransactionDTO::new).;// FINALIZAR
    }

    @GetMapping("/total")
    public BigDecimal total(@PathVariable Long id){
        return balance(id);
    }
}
