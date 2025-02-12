package com.example.capstone.controllers;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.bo.CardResponse;
import com.example.capstone.bo.UpdateUserRequest;
import com.example.capstone.bo.UserResponse;
import com.example.capstone.services.CardService;
import com.example.capstone.services.UserService;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;
    private final CardService cardService;

    public UserController(UserService userService, CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }


    @GetMapping("/view/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = UserService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view-all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/update/{user_id}")
    public ResponseEntity<UserResponse> updateUserProfile(@PathVariable Long user_id,
                                                          @RequestBody UpdateUserRequest updateRequest) {
        UserResponse updatedUser = userService.updateUser(user_id, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        UserResponse response = userService.getCurrentUser(authentication);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(Authentication authentication,
                                                        @RequestBody UpdateUserRequest updateRequest) {
        UserResponse updatedUser = userService.updateCurrentUser(authentication, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/me/cards")
    public ResponseEntity<List<CardResponse>> getCurrentUserCards(Authentication authentication) {
        UserEntity user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        List<CardResponse> cards = cardService.getUserCards(user);
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/me/card-issuance-limit")
    public ResponseEntity<Object> getCardIssuanceLimit(Authentication authentication) {
        UserEntity user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        return ResponseEntity.ok(Map.of(
            "monthlyLimit", user.getMonthlyCardIssuanceLimit(),
            "currentMonthUsage", user.getCurrentMonthCardIssuance()
        ));
    }

    @PostMapping("/me/connect-bank")
    public ResponseEntity<?> connectBankAccount(Authentication authentication, @RequestBody Map<String, String> request) {
        try {
            String bankAccountUsername = request.get("bankAccountUsername");
            if (bankAccountUsername == null) {
                return ResponseEntity.badRequest().body("Bank account username is required");
            }
            UserResponse response = userService.connectBankAccount(authentication, bankAccountUsername);
            return ResponseEntity.ok(Map.of(
                "message", "Bank account connected successfully",
                "bankAccountNumber", response.getBankAccountNumber(),
                "bankAccountUsername", response.getBankAccountUsername()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while connecting bank account");
        }
    }

    @PostMapping("/me/disconnect-bank")
    public ResponseEntity<?> disconnectBankAccount(Authentication authentication) {
        try {
            UserResponse response = userService.disconnectBankAccount(authentication);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while disconnecting bank account");
        }
    }

}
