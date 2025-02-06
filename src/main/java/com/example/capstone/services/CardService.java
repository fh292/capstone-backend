package com.example.capstone.services;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.authentication.repositories.UserRepository;
import com.example.capstone.bo.*;
import com.example.capstone.entities.CardEntity;
import com.example.capstone.repositories.CardRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CardService {

    private static CardRepository cardRepository = null;
    private static CardEntity cardEnitity = null;


    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public CardResponse createBurnerCard(BurnerCardRequest request, UserEntity user) {
        if (user == null) {
            throw new IllegalArgumentException("User must be provided to create a card.");
        }
        if (request.getDurationLimit() == null) {
            throw new IllegalArgumentException("Duration limit is required for burner cards");
        }

        CardEntity card = new CardEntity();
        card.setCardType("BURNER");
        card.setCardName(request.getCardName());
        card.setSpendingLimit(request.getSpendingLimit());
        card.setRemainingLimit(request.getSpendingLimit());
        card.setExpiryDate(request.getExpiryDate());
        card.setCreatedAt(LocalDateTime.now());
        card.setBankAccountNumber(request.getBankAccountNumber());
        card.setCardColor(request.getCardColor());
        card.setCardIcon(request.getCardIcon());
        card.setShared(request.getIsShared());
        card.setPer_transaction(request.getPer_transaction());
        card.setPer_day(request.getPer_day());
        card.setPer_week(request.getPer_week());
        card.setPer_month(request.getPer_month());
        card.setPer_year(request.getPer_year());
        card.setTotal(request.getTotal());
        card.setPaused(false);
        card.setClosed(false);

        card.setUser(user);
        card.setDurationLimit(request.getDurationLimit());
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
        card.setSpendingLimit(request.getSpendingLimit());
        card.setRemainingLimit(request.getSpendingLimit());
        card.setExpiryDate(request.getExpiryDate());
        card.setCreatedAt(LocalDateTime.now());
        card.setBankAccountNumber(request.getBankAccountNumber());
        card.setCardColor(request.getCardColor());
        card.setCardIcon(request.getCardIcon());
        card.setShared(request.getIsShared());
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
        card.setSpendingLimit(request.getSpendingLimit());
        card.setRemainingLimit(request.getSpendingLimit());
        card.setExpiryDate(request.getExpiryDate());
        card.setCreatedAt(LocalDateTime.now());
        card.setBankAccountNumber(request.getBankAccountNumber());
        card.setCardColor(request.getCardColor());
        card.setCardIcon(request.getCardIcon());
        card.setShared(request.getIsShared());
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
        card.setSpendingLimit(request.getSpendingLimit());
        card.setRemainingLimit(request.getSpendingLimit());
        card.setExpiryDate(request.getExpiryDate());
        card.setCreatedAt(LocalDateTime.now());
        card.setBankAccountNumber(request.getBankAccountNumber());
        card.setCardColor(request.getCardColor());
        card.setCardIcon(request.getCardIcon());
        card.setShared(request.getIsShared());
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
//        System.out.println("Updated Card ID: " + card.getId());
//        System.out.println("Updated Card Name: " + card.getCardName());
//        System.out.println("Updated Card Color: " + card.getCardColor());
//        System.out.println("Updated Card Icon: " + card.getCardIcon());

        return savedCard;
    }

    public CardLimitResponse updateCardLimit(Long cardId, CardLimitRequest request, UserEntity user) {
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("Card not found with ID: " + cardId));

        if (user == null || !card.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not have permission to update this card.");
        }

        // Update limits if provided
        if (request.getSpendingLimit() != null) {
            card.setSpendingLimit(request.getSpendingLimit());
        }
        if (request.getPer_transaction() != null) {
            card.setPer_transaction(request.getPer_transaction());
        }
        if (request.getPer_day() != null) {
            card.setPer_day(request.getPer_day());
        }
        if (request.getPer_week() != null) {
            card.setPer_week(request.getPer_week());
        }
        if (request.getPer_month() != null) {
            card.setPer_month(request.getPer_month());
        }
        if (request.getPer_year() != null) {
            card.setPer_year(request.getPer_year());
        }
        if (request.getTotal() != null) {
            card.setTotal(request.getTotal());
        }

        cardRepository.save(card);

        // Return a CardLimitResponse
        return new CardLimitResponse(
                card.getId(),
                card.getSpendingLimit(),
                card.getRemainingLimit(),
                card.getPer_transaction(),
                card.getPer_day(),
                card.getPer_week(),
                card.getPer_month(),
                card.getPer_year(),
                card.getTotal()
        );
    }









}
