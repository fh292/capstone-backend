package com.example.capstone.services;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.authentication.repositories.UserRepository;
import com.example.capstone.bo.TransactionRequest;
import com.example.capstone.bo.TransactionResponse;
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

    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setMerchantName(transactionRequest.getMerchantName());
        transactionEntity.setType(transactionRequest.getType());
        transactionEntity.setStatus(transactionRequest.getStatus());
        transactionEntity.setAmount(transactionRequest.getAmount());
        transactionEntity.setDescription(transactionRequest.getDescription());
        transactionEntity.setCreatedAt(LocalDateTime.now());
        CardEntity card = cardRepository.findById(transactionRequest.getCardId()).orElse(null);
        transactionEntity.setCard(card);
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

        // The default status is "pending"
        transactionEntity.setStatus("pending");

        // Begin checks in specified order

        // (A) Check if the card is closed
        if(card.getClosed()) {
            transactionEntity.setStatus("failed");
            transactionEntity.setDescription("Card is closed");
            transactionEntity = transactionRepository.save(transactionEntity);
            return new TransactionResponse(transactionEntity);
        }
        // (B) Check if the card is paused
        if(card.getPaused()) {
            transactionEntity.setStatus("failed");
            transactionEntity.setDescription("Card is paused");
            transactionEntity = transactionRepository.save(transactionEntity);
            return new TransactionResponse(transactionEntity);
        }
        //(C) Check the card type (Can either be CATEGORY_LOCKED, LOCATION_LOCKED, MERCHANT_LOCKED, or BURNER
        if(card.getCardType().equals("CATEGORY_LOCKED")) {
            // Check if the transaction is within the category
            if(!card.getCategoryName().equals(transactionEntity.getMerchantName())) {
                transactionEntity.setStatus("failed");
                transactionEntity.setDescription("Transaction is not within the category");
                transactionEntity = transactionRepository.save(transactionEntity);
                return new TransactionResponse(transactionEntity);
            }
        }
        else if(card.getCardType().equals("LOCATION_LOCKED")) {
            // Check if the transaction is within the location
            // TODO: implement locaiton check
//            if(!CardEntity.isWithinLocation(transactionEntity.getLatitude(), transactionEntity.getLongitude(), transactionEntity.getRadius())) {
                transactionEntity.setStatus("success");
                transactionEntity.setDescription("Transaction is within the location");
                transactionEntity = transactionRepository.save(transactionEntity);
                return new TransactionResponse(transactionEntity);
//            }
        }
        else if(card.getCardType().equals("MERCHANT_LOCKED")) {
            // Check if the transaction is within the merchant
            if(!card.getMerchantName().equals(transactionEntity.getMerchantName())) {
                transactionEntity.setStatus("failed");
                transactionEntity.setDescription("Transaction is not within the merchant");
                transactionEntity = transactionRepository.save(transactionEntity);
                return new TransactionResponse(transactionEntity);
            }
        }
        else if(card.getCardType().equals("BURNER")) {
            // Check if the transaction is within the spending limit
            if(card.getRemainingLimit() < transactionEntity.getAmount()) {
                transactionEntity.setStatus("failed");
                transactionEntity.setDescription("Transaction is not within the spending limit");
                transactionEntity = transactionRepository.save(transactionEntity);
                return new TransactionResponse(transactionEntity);
            }
        }
        else {
            transactionEntity.setStatus("failed");
            transactionEntity.setDescription("Card type is invalid");
            transactionEntity = transactionRepository.save(transactionEntity);
            return new TransactionResponse(transactionEntity);
        }
        // (G) Check for spend limits
        if(card.getPer_transaction() != null) {
            if(card.getPer_transaction() < transactionEntity.getAmount()) {
                transactionEntity.setStatus("failed");
                transactionEntity.setDescription("Transaction is not within the per transaction limit");
                transactionEntity = transactionRepository.save(transactionEntity);
                return new TransactionResponse(transactionEntity);
            }
            //(H) Update the spent amount on the card
            card.setRemainingLimit(card.getRemainingLimit() - transactionEntity.getAmount());

// (I) If burner at the end after approving the transaction close the card.
            if(card.getCardType().equals("BURNER")) {
                card.setClosed(true);
            }
            cardRepository.save(card);
        }
        transactionEntity.setStatus("success");
        transactionEntity = transactionRepository.save(transactionEntity);
        return new TransactionResponse(transactionEntity);
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found" + id);
        }
        transactionRepository.deleteById(id);
    }
}
