package com.example.capstone.services;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.bo.BurnerCardRequest;
import com.example.capstone.bo.BurnerCardResponse;
import com.example.capstone.bo.CardLimitUpdateRequest;
import com.example.capstone.bo.CardResponse;
import com.example.capstone.bo.CardUpdateRequest;
import com.example.capstone.bo.CategoryLockedCardRequest;
import com.example.capstone.bo.CategoryLockedCardResponse;
import com.example.capstone.bo.LocationLockedCardRequest;
import com.example.capstone.bo.LocationLockedCardResponse;
import com.example.capstone.bo.MerchantLockedCardRequest;
import com.example.capstone.bo.MerchantLockedCardResponse;
import com.example.capstone.entities.CardEntity;
import com.example.capstone.repositories.CardRepository;

@Service
public class CardService {

    private static final String CARD_PREFIX = "4707350";
    private final CardRepository cardRepository;
    private final SecureRandom random;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
        this.random = new SecureRandom();
    }

    public CardResponse createBurnerCard(BurnerCardRequest request, UserEntity user) {
        if (user == null) {
            throw new IllegalArgumentException("User must be provided to create a card.");
        }

        CardEntity card = new CardEntity();
        card.setCardType("BURNER");
        card.setCardName(request.getCardName());
        card.setCardNumber(generateCardNumber());
        card.setCvv(generateCVV());
        card.setExpiryDate(generateExpiryDate());
        card.setCreatedAt(LocalDateTime.now());
        card.setCardColor(request.getCardColor());
        card.setCardIcon(request.getCardIcon());
        card.setPer_transaction(request.getPer_transaction());
        card.setPer_day(request.getPer_day());
        card.setPer_week(request.getPer_week());
        card.setPer_month(request.getPer_month());
        card.setPer_year(request.getPer_year());
        card.setTotal(request.getTotal());
        card.setPaused(false);
        card.setClosed(false);
        card.setUser(user);
        card.setLimitSetAt(LocalDateTime.now());

        card = cardRepository.save(card);
        return new BurnerCardResponse(card);
    }

    public CardResponse createMerchantLockedCard(MerchantLockedCardRequest request, UserEntity user) {
        if (user == null) {
            throw new IllegalArgumentException("User must be provided to create a card.");
        }
        if (request.getMerchantName() == null) {
            throw new IllegalArgumentException("Merchant name is required for merchant-locked cards");
        }

        CardEntity card = new CardEntity();
        card.setCardType("MERCHANT_LOCKED");
        card.setCardName(request.getCardName());
        card.setCardNumber(generateCardNumber());
        card.setCvv(generateCVV());
        card.setExpiryDate(generateExpiryDate());
        card.setCreatedAt(LocalDateTime.now());
        card.setCardColor(request.getCardColor());
        card.setCardIcon(request.getCardIcon());
        card.setPer_transaction(request.getPer_transaction());
        card.setPer_day(request.getPer_day());
        card.setPer_week(request.getPer_week());
        card.setPer_month(request.getPer_month());
        card.setPer_year(request.getPer_year());
        card.setTotal(request.getTotal());
        card.setPaused(false);
        card.setClosed(false);
        card.setUser(user);
        card.setMerchantName(request.getMerchantName());
        card.setLimitSetAt(LocalDateTime.now());

        card = cardRepository.save(card);
        return new MerchantLockedCardResponse(card);
    }

    public CardResponse createLocationLockedCard(LocationLockedCardRequest request, UserEntity user) {
        if (user == null) {
            throw new IllegalArgumentException("User must be provided to create a card.");
        }
        if (request.getLatitude() == null || request.getLongitude() == null || request.getRadius() == null) {
            throw new IllegalArgumentException("Latitude, longitude, and radius are required for location-locked cards");
        }

        CardEntity card = new CardEntity();
        card.setCardType("LOCATION_LOCKED");
        card.setCardName(request.getCardName());
        card.setCardNumber(generateCardNumber());
        card.setCvv(generateCVV());
        card.setExpiryDate(generateExpiryDate());
        card.setCreatedAt(LocalDateTime.now());
        card.setCardColor(request.getCardColor());
        card.setCardIcon(request.getCardIcon());
        card.setPer_transaction(request.getPer_transaction());
        card.setPer_day(request.getPer_day());
        card.setPer_week(request.getPer_week());
        card.setPer_month(request.getPer_month());
        card.setPer_year(request.getPer_year());
        card.setTotal(request.getTotal());
        card.setPaused(false);
        card.setClosed(false);
        card.setUser(user);
        card.setLatitude(request.getLatitude());
        card.setLongitude(request.getLongitude());
        card.setRadius(request.getRadius());
        card.setLimitSetAt(LocalDateTime.now());

        card = cardRepository.save(card);
        return new LocationLockedCardResponse(card);
    }

    public CardResponse createCategoryLockedCard(CategoryLockedCardRequest request, UserEntity user) {
        if (user == null) {
            throw new IllegalArgumentException("User must be provided to create a card.");
        }
        if (request.getCategoryName() == null) {
            throw new IllegalArgumentException("Category name is required for category-locked cards");
        }

        CardEntity card = new CardEntity();
        card.setCardType("CATEGORY_LOCKED");
        card.setCardName(request.getCardName());
        card.setCardNumber(generateCardNumber());
        card.setCvv(generateCVV());
        card.setExpiryDate(generateExpiryDate());
        card.setCreatedAt(LocalDateTime.now());
        card.setCardColor(request.getCardColor());
        card.setCardIcon(request.getCardIcon());
        card.setPer_transaction(request.getPer_transaction());
        card.setPer_day(request.getPer_day());
        card.setPer_week(request.getPer_week());
        card.setPer_month(request.getPer_month());
        card.setPer_year(request.getPer_year());
        card.setTotal(request.getTotal());
        card.setPaused(false);
        card.setClosed(false);
        card.setUser(user);
        card.setCategoryName(request.getCategoryName());
        card.setLimitSetAt(LocalDateTime.now());

        card = cardRepository.save(card);
        return new CategoryLockedCardResponse(card);
    }

    public CardEntity updateCard(Long cardId, CardUpdateRequest request, UserEntity user) {
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found with ID: " + cardId));

        if (user == null || !card.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not have permission to update this card.");
        }

        if (request.getCardName() != null && !request.getCardName().isBlank()) {
            card.setCardName(request.getCardName());
        }
        if (request.getCardColor() != null && !request.getCardColor().isBlank()) {
            card.setCardColor(request.getCardColor());
        }
        if (request.getCardIcon() != null && !request.getCardIcon().isBlank()) {
            card.setCardIcon(request.getCardIcon());
        }

        CardEntity savedCard = cardRepository.save(card);
        System.out.println("Updated Card ID: " + card.getId());
        System.out.println("Updated Card Name: " + card.getCardName());
        System.out.println("Updated Card Color: " + card.getCardColor());
        System.out.println("Updated Card Icon: " + card.getCardIcon());

        return savedCard;
    }

    public CardEntity updateCardLimit(Long cardId, CardLimitUpdateRequest request, UserEntity user) {
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found with ID: " + cardId));

        if (user == null || !card.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not have permission to update this card.");
        }

        if (request.getLimitType() == null) {
            throw new IllegalArgumentException("Limit type must be provided.");
        }

        if (!request.getRemoveLimit() && request.getAmount() == null) {
            throw new IllegalArgumentException("Either amount must be provided or removeLimit must be true.");
        }

        // Reset all limits to zero first
        card.setPer_transaction(0.0);
        card.setPer_day(0.0);
        card.setPer_week(0.0);
        card.setPer_month(0.0);
        card.setPer_year(0.0);
        card.setTotal(0.0);

        // If we're removing the limit, we can return now since all limits are already zero
        if (request.getRemoveLimit()) {
            card.setLimitSetAt(LocalDateTime.now());
            return cardRepository.save(card);
        }

        // Set the requested limit
        switch (request.getLimitType().toUpperCase()) {
            case "PER_TRANSACTION":
                card.setPer_transaction(request.getAmount());
                break;
            case "PER_DAY":
                card.setPer_day(request.getAmount());
                break;
            case "PER_WEEK":
                card.setPer_week(request.getAmount());
                break;
            case "PER_MONTH":
                card.setPer_month(request.getAmount());
                break;
            case "PER_YEAR":
                card.setPer_year(request.getAmount());
                break;
            case "TOTAL":
                card.setTotal(request.getAmount());
                break;
            default:
                throw new IllegalArgumentException("Invalid limit type. Must be one of: PER_TRANSACTION, PER_DAY, PER_WEEK, PER_MONTH, PER_YEAR, TOTAL");
        }

        card.setLimitSetAt(LocalDateTime.now());
        return cardRepository.save(card);
    }

    public CardEntity toggleCardPause(Long cardId, UserEntity user) {
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found with ID: " + cardId));

        if (user == null || !card.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not have permission to update this card.");
        }

        if (card.getClosed()) {
            throw new IllegalArgumentException("Cannot pause/unpause a closed card.");
        }

        card.setPaused(!card.getPaused()); // Toggle the pause state
        return cardRepository.save(card);
    }

    public CardEntity closeCard(Long cardId, UserEntity user) {
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found with ID: " + cardId));

        if (user == null || !card.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not have permission to close this card.");
        }

        if (card.getClosed()) {
            throw new IllegalArgumentException("Card is already closed.");
        }

        card.setClosed(true);
        card.setPaused(true); // Also pause the card when closing
        return cardRepository.save(card);
    }

    public List<CardResponse> getUserCards(UserEntity user) {
        if (user == null) {
            throw new IllegalArgumentException("User must be provided to get cards.");
        }
        return cardRepository.findByUser(user).stream()
                .map(card -> {
                    switch (card.getCardType()) {
                        case "BURNER":
                            return new BurnerCardResponse(card);
                        case "MERCHANT_LOCKED":
                            return new MerchantLockedCardResponse(card);
                        case "LOCATION_LOCKED":
                            return new LocationLockedCardResponse(card);
                        case "CATEGORY_LOCKED":
                            return new CategoryLockedCardResponse(card);
                        default:
                            return new CardResponse(card);
                    }
                })
                .collect(Collectors.toList());
    }

    private String generateCardNumber() {
        StringBuilder builder = new StringBuilder(CARD_PREFIX);
        for (int i = CARD_PREFIX.length(); i < 16; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    private String generateCVV() {
        return String.format("%03d", random.nextInt(1000));
    }

    private LocalDate generateExpiryDate() {
        return LocalDate.now().plusYears(3);
    }
}
