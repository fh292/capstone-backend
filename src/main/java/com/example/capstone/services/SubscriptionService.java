package com.example.capstone.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.bo.SubscriptionResponse;
import com.example.capstone.entities.CardEntity;
import com.example.capstone.entities.SubscriptionEntity;
import com.example.capstone.entities.TransactionEntity;
import com.example.capstone.repositories.SubscriptionRepository;
import com.example.capstone.repositories.TransactionRepository;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationService notificationService;

    public SubscriptionService(
            SubscriptionRepository subscriptionRepository,
            TransactionRepository transactionRepository,
            NotificationService notificationService) {
        this.subscriptionRepository = subscriptionRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
    }

    public List<SubscriptionResponse> getUserSubscriptions(UserEntity user) {
        return subscriptionRepository.findByUserAndIsActiveTrue(user).stream()
                .map(SubscriptionResponse::new)
                .collect(Collectors.toList());
    }

    public void processRecurringTransaction(TransactionEntity transaction) {
        if (!Boolean.TRUE.equals(transaction.getRecurring()) || !"APPROVED".equals(transaction.getStatus())) {
            return;
        }

        // Check if subscription already exists for this merchant and card
        List<SubscriptionEntity> existingSubscriptions = subscriptionRepository.findByUser(transaction.getUser());
        boolean subscriptionExists = existingSubscriptions.stream()
                .anyMatch(sub -> sub.getMerchant().equals(transaction.getMerchant())
                        && sub.getCard().getId().equals(transaction.getCard().getId())
                        && sub.getIsActive());

        if (!subscriptionExists) {
            createNewSubscription(transaction);
        }
    }

    public void handleCardClosure(CardEntity card) {
        List<SubscriptionEntity> activeSubscriptions = subscriptionRepository.findByUser(card.getUser()).stream()
                .filter(sub -> sub.getCard().getId().equals(card.getId()) && sub.getIsActive())
                .collect(Collectors.toList());

        if (!activeSubscriptions.isEmpty()) {
            for (SubscriptionEntity subscription : activeSubscriptions) {
                subscription.setIsActive(false);
                subscriptionRepository.save(subscription);

                // Notify user about subscription deactivation
                try {
                    notificationService.sendNotification(
                        card.getUser(),
                        "Subscription Deactivated",
                        String.format("Your subscription to %s has been deactivated because the linked card was closed. " +
                                    "To continue the subscription, please update your payment method.",
                                    subscription.getMerchant()),
                        java.util.Map.of(
                            "subscriptionId", subscription.getId(),
                            "merchant", subscription.getMerchant(),
                            "cardId", card.getId(),
                            "cardName", card.getCardName()
                        )
                    );
                } catch (Exception e) {
                    System.out.println("Error sending notification: " + e.getMessage());
                }
            }
        }
    }

    private void createNewSubscription(TransactionEntity transaction) {
        SubscriptionEntity subscription = new SubscriptionEntity();
        subscription.setMerchant(transaction.getMerchant());
        subscription.setAmount(transaction.getAmount());
        subscription.setCategory(transaction.getCategory());
        subscription.setLastChargeDate(transaction.getCreatedAt());
        subscription.setNextChargeDate(calculateNextChargeDate(transaction.getCreatedAt()));
        subscription.setBillingCycle("MONTHLY"); // Default to monthly, can be enhanced based on transaction pattern
        subscription.setIsActive(true);
        subscription.setCard(transaction.getCard());
        subscription.setUser(transaction.getUser());

        subscriptionRepository.save(subscription);
    }

    private LocalDateTime calculateNextChargeDate(LocalDateTime lastChargeDate) {
        // Default to monthly billing cycle
        return lastChargeDate.plusMonths(1);
    }
}