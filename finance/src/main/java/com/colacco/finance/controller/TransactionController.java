package com.colacco.finance.controller;

import com.colacco.finance.dto.TransactionDTO;
import com.colacco.finance.dto.TransactionOutputDTO;
import com.colacco.finance.dto.TransactionPUTDTO;
import com.colacco.finance.models.Transaction;
import com.colacco.finance.models.TransactionType;
import com.colacco.finance.repository.TransactionRepository;
import com.colacco.finance.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/{usuarioId}")
public class TransactionController {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public TransactionRepository transactionRepository;

    private BigDecimal balance(Long usuarioId){
        BigDecimal total = BigDecimal.valueOf(0);
        List<Transaction> transactionList = transactionRepository.findAll().stream().filter(transaction -> transaction.getUser().equals(userRepository.getReferenceById(usuarioId))).toList();

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
    public void launch(@RequestBody @Valid TransactionDTO transactionDTO, @PathVariable Long usuarioId, @PathVariable int io){
        if (io == 1){
            transactionRepository.save(new Transaction(transactionDTO.value(), TransactionType.INPUT, userRepository.getReferenceById(usuarioId)));
        } else {
            if (balance(usuarioId).compareTo(transactionDTO.value()) > 0){
                transactionRepository.save(new Transaction(transactionDTO.value(), TransactionType.OUTPUT, userRepository.getReferenceById(usuarioId)));
            } else {
                System.out.println("Saldo insuficiente");
            }
        }
    }

    @PutMapping
    @Transactional
    public void update(@RequestBody @Valid TransactionPUTDTO transactionPUTDTO){
        Transaction transaction = transactionRepository.getReferenceById(transactionPUTDTO.id());
        Long userId = transaction.getUser().getId();

        if(transaction.getTransactionType().equals(TransactionType.OUTPUT)){
            if (balance(userId).compareTo(transactionPUTDTO.value()) > 0){
                transaction.update(transactionPUTDTO);
            } else {
                System.out.println("Saldo insuficiente");
            }
        } else {
            transaction.update(transactionPUTDTO);
        }
    }

    @DeleteMapping("/{transactionId}")
    @Transactional
    public void remove(@PathVariable Long transactionId){
        Transaction transaction = transactionRepository.getReferenceById(transactionId);
        Long userId = transaction.getUser().getId();

        if (transaction.getTransactionType().equals(TransactionType.INPUT)){
            if (balance(userId).subtract(transaction.getValue()).compareTo(BigDecimal.valueOf(0)) >= 0){
                transactionRepository.delete(transaction);
            } else {
                System.out.println("Saldo insuficiente");
            }
        }

        transactionRepository.delete(transaction);
    }

    @GetMapping("/list")
    public Page<TransactionOutputDTO> list(@PathVariable Long usuarioId, Pageable pageable){
        return transactionRepository.findByUser(userRepository.getReferenceById(usuarioId), pageable).map(TransactionOutputDTO::new);
    }

    @GetMapping("/total")
    public BigDecimal total(@PathVariable Long usuarioId){
        return balance(usuarioId);
    }
}
