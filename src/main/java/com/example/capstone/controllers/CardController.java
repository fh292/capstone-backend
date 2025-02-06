package com.example.capstone.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.bo.BurnerCardRequest;
import com.example.capstone.bo.CardLimitUpdateRequest;
import com.example.capstone.bo.CardResponse;
import com.example.capstone.bo.CardUpdateRequest;
import com.example.capstone.bo.CategoryLockedCardRequest;
import com.example.capstone.bo.CategoryLockedCardResponse;
import com.example.capstone.bo.LocationLockedCardRequest;
import com.example.capstone.bo.LocationLockedCardResponse;
import com.example.capstone.bo.MerchantLockedCardRequest;
import com.example.capstone.entities.CardEntity;
import com.example.capstone.services.CardService;
import com.example.capstone.services.UserService;

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

    @PutMapping("/update/{cardId}/limit")
    public ResponseEntity<CardEntity> updateCardLimit(
            @PathVariable Long cardId,
            @RequestBody CardLimitUpdateRequest request) {
        try {
            UserEntity user = getAuthenticatedUser();
            CardEntity updatedCard = cardService.updateCardLimit(cardId, request, user);
            return ResponseEntity.ok(updatedCard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{cardId}/toggle-pause")
    public ResponseEntity<?> toggleCardPause(@PathVariable Long cardId) {
        try {
            UserEntity user = getAuthenticatedUser();
            CardEntity updatedCard = cardService.toggleCardPause(cardId, user);
            return ResponseEntity.ok(updatedCard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the card");
        }
    }

    @PutMapping("/{cardId}/close")
    public ResponseEntity<?> closeCard(@PathVariable Long cardId) {
        try {
            UserEntity user = getAuthenticatedUser();
            CardEntity updatedCard = cardService.closeCard(cardId, user);
            return ResponseEntity.ok(updatedCard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while closing the card");
        }
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

    private UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }






}
