package com.example.capstone.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.bo.TransactionRequest;
import com.example.capstone.bo.TransactionResponse;
import com.example.capstone.entities.CardEntity;
import com.example.capstone.entities.TransactionEntity;
import com.example.capstone.repositories.CardRepository;
import com.example.capstone.repositories.TransactionRepository;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, CardRepository cardRepository) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId).stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getAuthenticatedUserTransactions(UserEntity user) {
        return transactionRepository.findByUser(user).stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

    public List<TransactionResponse> getTransactionsByCardId(Long cardId, UserEntity user) {
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        // Verify the card belongs to the user
        if (!card.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Card does not belong to user");
        }

        return transactionRepository.findByCard(card).stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

    public TransactionResponse getTransactionById(Long id) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElse(null);
        return transaction != null ? new TransactionResponse(transaction) : null;
    }

    public TransactionResponse processTransaction(TransactionRequest request) {
        // Find card by card number
        CardEntity card = cardRepository.findByCardNumber(request.getCardNumber())
                .orElseThrow(() -> new RuntimeException("Card not found"));

        // Basic card validation
        String validationResult = validateCard(card, request);
        if (validationResult != null) {
            return createDeclinedTransaction(request, card, validationResult);
        }

        // Card type specific validation
        String typeValidationResult = validateCardByType(card, request);
        if (typeValidationResult != null) {
            return createDeclinedTransaction(request, card, typeValidationResult);
        }

        // If this is a merchant-locked card's first transaction, set the merchant
        if ("merchant_locked".equalsIgnoreCase(card.getCardType()) && card.getMerchantName() == null) {
            card.setMerchantName(request.getMerchant());
            cardRepository.save(card);
        }

        // Create and save approved transaction
        return createApprovedTransaction(request, card);
    }

    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found");
        }
        transactionRepository.deleteById(id);
    }

    private String validateCard(CardEntity card, TransactionRequest request) {
        // Validate card is not expired
        if (card.getExpiryDate().isBefore(LocalDateTime.now().toLocalDate())) {
            return "Card expired";
        }

        // Validate CVV
        if (!card.getCvv().equals(request.getCvv())) {
            return "Invalid CVV";
        }

        // Validate card is not paused or closed
        if (Boolean.TRUE.equals(card.getClosed())) {
            return "Card is closed";
        }
        if (Boolean.TRUE.equals(card.getPaused())) {
            return "Card is paused";
        }

        // Validate spending limits
        if (card.getPer_transaction() != null && request.getAmount() > card.getPer_transaction()) {
            return "Transaction amount exceeds per-transaction limit";
        }

        // Add more validations for daily, weekly, monthly limits
        // This would require tracking spending over time periods

        return null;
    }

    private String validateCardByType(CardEntity card, TransactionRequest request) {
        switch (card.getCardType().toLowerCase()) {
            case "burner":
                return validateBurnerCard(card);
            case "merchant_locked":
                return validateMerchantLockedCard(card, request);
            case "category_locked":
                return validateCategoryLockedCard(card, request);
            case "location_locked":
                return validateLocationLockedCard(card, request);
            default:
                return null;
        }
    }

    private String validateBurnerCard(CardEntity card) {
        // Check if there's any approved transaction
        boolean hasApprovedTransaction = card.getTransaction().stream()
                .anyMatch(t -> "APPROVED".equals(t.getStatus()));

        if (hasApprovedTransaction) {
            return "Burner card has already been used";
        }
        return null;
    }

    private String validateMerchantLockedCard(CardEntity card, TransactionRequest request) {
        // If merchant is not set yet, this is the first transaction
        if (card.getMerchantName() == null) {
            return null; // Allow the transaction, merchant will be set after validation
        }

        // For subsequent transactions, validate against the locked merchant
        if (!card.getMerchantName().equalsIgnoreCase(request.getMerchant())) {
            return "Card can only be used with merchant: " + card.getMerchantName();
        }
        return null;
    }

    private String validateCategoryLockedCard(CardEntity card, TransactionRequest request) {
        if (!card.getCategoryName().equalsIgnoreCase(request.getCategory())) {
            return "Card can only be used for category: " + card.getCategoryName();
        }
        return null;
    }

    private String validateLocationLockedCard(CardEntity card, TransactionRequest request) {
        if (request.getLatitude() == null || request.getLongitude() == null) {
            return "Location coordinates required for this card type";
        }

        // Calculate distance between transaction location and card's allowed location
        double distance = calculateDistance(
            card.getLatitude(), card.getLongitude(),
            request.getLatitude(), request.getLongitude()
        );

        if (distance > card.getRadius()) {
            return "Transaction location outside allowed radius";
        }
        return null;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula to calculate distance between two points
        final int R = 6371; // Earth's radius in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    private TransactionResponse createDeclinedTransaction(TransactionRequest request, CardEntity card, String reason) {
        TransactionEntity transaction = createTransactionEntity(request, card);
        transaction.setStatus("DECLINED");
        transaction = transactionRepository.save(transaction);
        return new TransactionResponse(transaction, reason);
    }

    private TransactionResponse createApprovedTransaction(TransactionRequest request, CardEntity card) {
        TransactionEntity transaction = createTransactionEntity(request, card);
        transaction.setStatus("APPROVED");
        transaction = transactionRepository.save(transaction);

        // If this is a burner card, close it after the first approved transaction
        if ("BURNER".equalsIgnoreCase(card.getCardType())) {
            card.setClosed(true);
            card.setPaused(true);
            cardRepository.save(card);
        }

        return new TransactionResponse(transaction);
    }

    private TransactionEntity createTransactionEntity(TransactionRequest request, CardEntity card) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setMerchant(request.getMerchant());
        transaction.setAmount(request.getAmount());
        transaction.setRecurring(request.getIsRecurring());
        transaction.setDescription(request.getDescription());
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());
        transaction.setLongitude(request.getLongitude());
        transaction.setLatitude(request.getLatitude());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setCard(card);
        transaction.setUser(card.getUser()); // Set the user from the card
        return transaction;
    }
}
