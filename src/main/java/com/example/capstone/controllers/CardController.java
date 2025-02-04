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






}
