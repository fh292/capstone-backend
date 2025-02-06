package com.example.capstone.controllers;

import com.example.capstone.bo.SharedCardRequest;
import com.example.capstone.entities.SharedCardEntity;
import com.example.capstone.services.SharedCardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shared-cards")
public class SharedCardController {

    private final SharedCardService sharedCardService;

    public SharedCardController(SharedCardService sharedCardService) {
        this.sharedCardService = sharedCardService;
    }

    // Get all shared cards for a user.
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SharedCardRequest>> getSharedCardsForUser(@PathVariable Long userId) {
        List<SharedCardEntity> sharedCards = sharedCardService.getSharedCardsForUser(userId);
        return ResponseEntity.ok(sharedCards.stream().map(SharedCardRequest::new).toList());
    }
//Get all users a card is shared with.
    @GetMapping("/card/{cardId}")
    public ResponseEntity<List<SharedCardRequest>> getAllUsersForCard(@PathVariable Long cardId) {
        System.out.println("cardId: " + cardId);
        List<SharedCardRequest> sharedCards = sharedCardService.getAllUsersForCard(cardId);
        return ResponseEntity.ok(sharedCards);
    }
    // Delete a shared card By ID.
    @DeleteMapping("/{sharedCardId}")
    public ResponseEntity<Void> deleteSharedCard(@PathVariable Long sharedCardId) {
        sharedCardService.deleteSharedCard(sharedCardId);
        return ResponseEntity.noContent().build();
    }
}
