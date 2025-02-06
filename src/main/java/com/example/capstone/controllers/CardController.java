package com.example.capstone.controllers;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.bo.*;
import com.example.capstone.entities.CardEntity;
import com.example.capstone.services.CardService;
import com.example.capstone.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/card")
@RestController
public class CardController {

    private final CardService cardService;
    private final UserService userService;
    public CardController(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    @PostMapping("/create/burner")
    public ResponseEntity<CardResponse> createBurnerCard(@RequestBody BurnerCardRequest request) {
        UserEntity user = getAuthenticatedUser();
        CardResponse response = cardService.createBurnerCard(request, user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create/merchant-locked")
    public ResponseEntity<CardResponse> createMerchantLockedCard(@RequestBody MerchantLockedCardRequest request) {
        UserEntity user = getAuthenticatedUser();
        CardResponse response = cardService.createMerchantLockedCard(request, user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create/location-locked")
    public ResponseEntity<CardResponse> createLocationLockedCard(@RequestBody LocationLockedCardRequest request) {
        UserEntity user = getAuthenticatedUser();
        CardResponse response = cardService.createLocationLockedCard(request, user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create/category-locked")
    public ResponseEntity<CardResponse> createCategoryLockedCard(@RequestBody CategoryLockedCardRequest request) {
        UserEntity user = getAuthenticatedUser();
        CardResponse response = cardService.createCategoryLockedCard(request, user);
        return ResponseEntity.ok(response);
    }

    private UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PutMapping("/update/{cardId}")
    public ResponseEntity<CardEntity> updateCard(
            @PathVariable Long cardId,
            @RequestBody CardUpdateRequest request) {
        try {
            UserEntity user = getAuthenticatedUser();
            CardEntity updatedCard = cardService.updateCard(cardId, request, user);
            return ResponseEntity.ok(updatedCard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/limits/{cardId}")
    public ResponseEntity<CardLimitResponse> updateCardLimit(@PathVariable Long cardId, @RequestBody CardLimitRequest request) {
        UserEntity user = getAuthenticatedUser();
        CardLimitResponse response = cardService.updateCardLimit(cardId, request, user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/pause/{cardId}")
    public ResponseEntity<PausedCardResponse> togglePause(@PathVariable Long cardId) {
        UserEntity user = getAuthenticatedUser();
        PausedCardResponse response = cardService.toggleCardPause(cardId, user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/close/{cardId}")
    public ResponseEntity<ClosedCardResponse> closeCard(@PathVariable Long cardId) {
        UserEntity user = getAuthenticatedUser();
        ClosedCardResponse response = cardService.closeCard(cardId, user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-location/{cardId}")
    public ResponseEntity<LocationLockedCardResponse> updateGeoLocation(
            @PathVariable Long cardId,
            @RequestBody LocationLockedCardResponse locationUpdate) {

        UserEntity user = getAuthenticatedUser();
        LocationLockedCardResponse response = cardService.updateGeoLocation(cardId, locationUpdate, user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-category/{cardId}")
    public ResponseEntity<CategoryLockedCardResponse> updateCategoryName(
            @PathVariable Long cardId,
            @RequestBody CategoryLockedCardResponse requestBody
    ) {
        CategoryLockedCardResponse response = cardService.updateCategoryName(cardId, requestBody);
        return ResponseEntity.ok(response);
    }











}
