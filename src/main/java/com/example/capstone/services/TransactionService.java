package com.example.capstone.services;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.authentication.repositories.UserRepository;
import com.example.capstone.bo.TransactionRequest;
import com.example.capstone.entities.CardEntity;
import com.example.capstone.entities.TransactionEntity;
import com.example.capstone.repositories.CardRepository;
import com.example.capstone.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
private final TransactionRepository transactionRepository;
private final CardRepository cardRepository;
private final UserRepository userRepository;

@Autowired
    public TransactionService(TransactionRepository transactionRepository, CardRepository cardRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }
    public List<TransactionRequest> getAllTransactions() {
        return transactionRepository.findAll().stream().map(TransactionRequest::new).collect(Collectors.toList());
    }

    public List<TransactionRequest> getTransactionsByUserId(Long userId) {
         List<TransactionEntity> transactionEntity = transactionRepository.findByUserId(userId);
        return transactionEntity.stream().map(TransactionRequest::new).collect(Collectors.toList());
    }
    public TransactionRequest getTransactionById(Long id) {
        TransactionEntity transactionEntity = transactionRepository.findById(id).orElse(null);
        return transactionEntity != null ? new TransactionRequest(transactionEntity) : null;
    }

    public TransactionRequest createTransaction(TransactionRequest transactionRequest) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setMerchantName(transactionRequest.getMerchantName());
        transactionEntity.setType(transactionRequest.getType());
        transactionEntity.setStatus(transactionRequest.getStatus());
        transactionEntity.setAmount(transactionRequest.getAmount());
        transactionEntity.setCreatedAt(LocalDateTime.now());
        transactionEntity.setCard(cardRepository.findById(transactionRequest.getCardId()).orElse(null));
        transactionEntity.setUser(userRepository.findById(transactionRequest.getUserId()).orElse(null));

        // Retrieve card and user by ID if required
        if (transactionRequest.getCardId() != null) {
            CardEntity cardEntity = cardRepository.findById(transactionRequest.getCardId()).orElseThrow(() -> new RuntimeException("Card not found" + transactionRequest.getCardId()));
            transactionEntity.setCard(cardEntity);
        }
        if (transactionRequest.getUserId() != null) {
            UserEntity userEntity = userRepository.findById(transactionRequest.getUserId()).orElseThrow(() -> new RuntimeException("User not found" + transactionRequest.getUserId()));
            transactionEntity.setUser(userEntity);
        }
        transactionEntity = transactionRepository.save(transactionEntity);
        return new TransactionRequest(transactionEntity);
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found" + id);
        }
        transactionRepository.deleteById(id);
    }
}
