package com.example.capstone.controllers;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.bo.CardResponse;
import com.example.capstone.bo.SubscriptionResponse;
import com.example.capstone.bo.UpdateUserRequest;
import com.example.capstone.bo.UserResponse;
import com.example.capstone.services.CardService;
import com.example.capstone.services.FileStorageService;
import com.example.capstone.services.SubscriptionService;
import com.example.capstone.services.UserService;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;
    private final CardService cardService;
    private final SubscriptionService subscriptionService;
    private final FileStorageService fileStorageService;

    public UserController(UserService userService, CardService cardService, SubscriptionService subscriptionService, FileStorageService fileStorageService) {
        this.userService = userService;
        this.cardService = cardService;
        this.subscriptionService = subscriptionService;
        this.fileStorageService = fileStorageService;
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

    @GetMapping("/me/subscriptions")
    public ResponseEntity<List<SubscriptionResponse>> getCurrentUserSubscriptions(Authentication authentication) {
        UserEntity user = userService.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        List<SubscriptionResponse> subscriptions = subscriptionService.getUserSubscriptions(user);
        return ResponseEntity.ok(subscriptions);
    }

    @PostMapping(value = "/me/profile-picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> updateProfilePicture(
            Authentication authentication,
            @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().build();
            }

            UserResponse response = userService.updateProfilePicture(authentication, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/me/profile-picture")
    public ResponseEntity<UserResponse> deleteProfilePicture(Authentication authentication) {
        try {
            UserResponse response = userService.deleteProfilePicture(authentication);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/uploads/{fileName:.+}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String fileName) {
        try {
            Resource resource = fileStorageService.loadFileAsResource(fileName);
            String contentType = determineContentType(fileName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String determineContentType(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        return switch (fileExtension) {
            case ".png" -> "image/png";
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".gif" -> "image/gif";
            case ".webp" -> "image/webp";
            default -> "application/octet-stream";
        };
    }
}
