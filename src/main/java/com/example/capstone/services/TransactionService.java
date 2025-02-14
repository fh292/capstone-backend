package com.example.capstone.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.authentication.repositories.UserRepository;
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
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final SubscriptionService subscriptionService;

    @Autowired
    public TransactionService(
            TransactionRepository transactionRepository,
            CardRepository cardRepository,
            UserRepository userRepository,
            NotificationService notificationService,
            SubscriptionService subscriptionService) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.subscriptionService = subscriptionService;
    }

    public Page<TransactionEntity> getAllTransactionsPg(int page, int size) {
        if (size <= 0) size = 10;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findAll(pageable);
    }


    @Cacheable(value = "transactions",key = "#id")
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

    @Cacheable(value = "transactions",key = "#user.id")
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

        // Check if user has a bank account connected
        if (card.getUser().getBankAccountUsername() == null || card.getUser().getBankAccountNumber() == null) {
            return createDeclinedTransaction(request, card, "No bank account connected. Please connect a bank account first.");
        }

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

    public Map<String, Object> analyzeTransactions(UserEntity user) {
        List<TransactionEntity> transactions = transactionRepository.findByUser(user);

        // Group transactions by merchant
        Map<String, Long> merchantFrequency = transactions.stream()
                .collect(Collectors.groupingBy(TransactionEntity::getMerchant, Collectors.counting()));

        // Group transactions by category
        Map<String, Double> categorySpending = transactions.stream()
                .collect(Collectors.groupingBy(TransactionEntity::getCategory, Collectors.summingDouble(TransactionEntity::getAmount)));

        // Find the most-used merchant and highest spending category
        String mostUsedMerchant = merchantFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("Unknown");

        String highestSpendingCategory = categorySpending.entrySet().stream()
                .max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("Unknown");

        // Prepare data for AI suggestion
        Map<String, Object> insights = new HashMap<>();
        insights.put("mostUsedMerchant", mostUsedMerchant);
        insights.put("highestSpendingCategory", highestSpendingCategory);
        insights.put("merchantFrequency", merchantFrequency);
        insights.put("categorySpending", categorySpending);

        return insights;
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

        // Validate total spending limit first
        if (card.getTotal() != null) {
            double totalSpent = calculateTotalSpending(card);
            if (totalSpent + request.getAmount() > card.getTotal()) {
                return "Transaction amount exceeds total card limit";
            }
        }

        // Validate per-transaction limit
        if (card.getPer_transaction() != null && request.getAmount() > card.getPer_transaction()) {
            return "Transaction amount exceeds per-transaction limit";
        }

        // Validate daily limit
        if (card.getPer_day() != null) {
            double dailySpent = calculateSpendingInPeriod(card, LocalDateTime.now().minusDays(1));
            if (dailySpent + request.getAmount() > card.getPer_day()) {
                return "Transaction amount exceeds daily limit";
            }
        }

        // Validate weekly limit
        if (card.getPer_week() != null) {
            double weeklySpent = calculateSpendingInPeriod(card, LocalDateTime.now().minusWeeks(1));
            if (weeklySpent + request.getAmount() > card.getPer_week()) {
                return "Transaction amount exceeds weekly limit";
            }
        }

        // Validate monthly limit
        if (card.getPer_month() != null) {
            double monthlySpent = calculateSpendingInPeriod(card, LocalDateTime.now().minusMonths(1));
            if (monthlySpent + request.getAmount() > card.getPer_month()) {
                return "Transaction amount exceeds monthly limit";
            }
        }

        // Validate yearly limit
        if (card.getPer_year() != null) {
            double yearlySpent = calculateSpendingInPeriod(card, LocalDateTime.now().minusYears(1));
            if (yearlySpent + request.getAmount() > card.getPer_year()) {
                return "Transaction amount exceeds yearly limit";
            }
        }

        return null;
    }

    private double calculateTotalSpending(CardEntity card) {
        return card.getTransaction().stream()
                .filter(t -> "APPROVED".equals(t.getStatus()))
                .mapToDouble(TransactionEntity::getAmount)
                .sum();
    }

    private double calculateSpendingInPeriod(CardEntity card, LocalDateTime startTime) {
        return card.getTransaction().stream()
                .filter(t -> "APPROVED".equals(t.getStatus()))
                .filter(t -> t.getCreatedAt().isAfter(startTime))
                .mapToDouble(TransactionEntity::getAmount)
                .sum();
    }

    private String validateCardByType(CardEntity card, TransactionRequest request) {
        return switch (card.getCardType().toLowerCase()) {
            case "burner" -> validateBurnerCard(card);
            case "merchant_locked" -> validateMerchantLockedCard(card, request);
            case "category_locked" -> validateCategoryLockedCard(card, request);
            case "location_locked" -> validateLocationLockedCard(card, request);
            default -> null;
        };
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
        transaction.setDeclineReason(reason);
        transaction = transactionRepository.save(transaction);

        // Send notification for declined transaction
        try {
            notificationService.sendNotification(
                card.getUser(),
                "Transaction Declined",
            String.format("Payment of KD %.2f at %s was declined. %s",
                request.getAmount(),
                request.getMerchant(),
                reason
            ),
            Map.of(
                "transactionId", transaction.getId(),
                "cardId", card.getId(),
                "amount", request.getAmount(),
                "merchant", request.getMerchant(),
                "status", "DECLINED",
                "reason", reason
                )
            );
        } catch (Exception e) {
            System.out.println("Error sending notification: " + e.getMessage());
        }

        return new TransactionResponse(transaction, reason);
    }

    private TransactionResponse createApprovedTransaction(TransactionRequest request, CardEntity card) {
        TransactionEntity transaction = createTransactionEntity(request, card);
        transaction.setStatus("APPROVED");
        transaction = transactionRepository.save(transaction);

        // Update the user's current daily and monthly spend
        UserEntity user = card.getUser();
        if(user.getCurrentDailySpend() == null) {
            user.setCurrentDailySpend(0.0);
        }
        if(user.getCurrentMonthlySpend() == null) {
            user.setCurrentMonthlySpend(0.0);
        }
        user.setCurrentDailySpend(user.getCurrentDailySpend() + request.getAmount());
        user.setCurrentMonthlySpend(user.getCurrentMonthlySpend() + request.getAmount());
        userRepository.save(user);

        // Process subscription if it's a recurring transaction
        subscriptionService.processRecurringTransaction(transaction);

        // Send notification for approved transaction
        try {
            notificationService.sendNotification(
                user,
                "Transaction Approved",
                String.format("Payment of KD %.2f at %s was approved.",
                request.getAmount(),
                request.getMerchant()
            ),
            Map.of(
                "transactionId", transaction.getId(),
                "cardId", card.getId(),
                "amount", request.getAmount(),
                "merchant", request.getMerchant(),
                "status", "APPROVED"
                )
            );
        } catch (Exception e) {
            System.out.println("Error sending notification: " + e.getMessage());
        }

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
