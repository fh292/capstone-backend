package com.example.capstone.controllers;

import com.example.capstone.bo.TransactionRequest;
import com.example.capstone.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequestMapping("/transaction")
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @GetMapping
    public ResponseEntity<List<TransactionRequest>> getAllTransactions() {
        List<TransactionRequest> transactionRequest = transactionService.getAllTransactions();
        return transactionRequest != null ? ResponseEntity.ok(transactionRequest) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionRequest> getTransactionById(@PathVariable Long id) {
       TransactionRequest transactionRequest = transactionService.getTransactionById(id);
        return transactionRequest != null ? ResponseEntity.ok(transactionRequest) : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionRequest>> getTransactionByUserId(@PathVariable Long userId) {
        //System.out.println("the id is " + userId);
        List<TransactionRequest> transactionRequest= transactionService.getTransactionsByUserId(userId);
        return transactionRequest != null ? ResponseEntity.ok(transactionRequest) : ResponseEntity.notFound().build();
    }
    @PostMapping
    public ResponseEntity<TransactionRequest> createTransaction(@RequestBody TransactionRequest transactionRequest) {
        TransactionRequest createdTransaction = transactionService.createTransaction(transactionRequest);
        return createdTransaction != null ? ResponseEntity.ok(createdTransaction) : ResponseEntity.badRequest().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }
}
