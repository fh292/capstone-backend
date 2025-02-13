package com.example.capstone.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.authentication.repositories.UserRepository;
import com.example.capstone.bo.UpdateUserRequest;
import com.example.capstone.bo.UserResponse;

@Service
public class UserService {
    private static UserRepository userRepository = null;
    public static UserResponse getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + id + " not found"));

        return new UserResponse(userEntity);
    }

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        UserService.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .filter(user -> "ROLE_USER".equals(user.getRole()))
                .map(UserResponse::new)
                .toList();
    }

    public UserResponse updateUser(Long userId, UpdateUserRequest updateRequest) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateRequest != null) {
            if (updateRequest.getFirstName() != null) user.setFirstName(updateRequest.getFirstName());
            if (updateRequest.getLastName() != null) user.setLastName(updateRequest.getLastName());
            if (updateRequest.getPhoneNumber() != null) user.setPhoneNumber(updateRequest.getPhoneNumber());
            if (updateRequest.getBankAccountUsername() != null) user.setBankAccountUsername(updateRequest.getBankAccountUsername());
            if (updateRequest.getSubscription() != null) user.setSubscription(updateRequest.getSubscription());
            if (updateRequest.getProfilePic() != null) user.setProfilePic(updateRequest.getProfilePic());
            if (updateRequest.getGender() != null) user.setGender(updateRequest.getGender());
            if (updateRequest.getDateOfBirth() != null) user.setDateOfBirth(updateRequest.getDateOfBirth());
        }

        userRepository.save(user);

        return new UserResponse(user);
    }

    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with ID " + userId + " not found");
        }
        userRepository.deleteById(userId);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserResponse getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return new UserResponse(userEntity);
    }

    public UserResponse updateCurrentUser(Authentication authentication, UpdateUserRequest updateRequest) {
        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        if (updateRequest.getCivilId() != null) {
            if (!updateRequest.getCivilId().equals(user.getCivilId())) {
                Optional<UserEntity> existingUser = userRepository.findByCivilId(updateRequest.getCivilId());
                if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
                    throw new IllegalArgumentException("Civil ID is already in use");
                }
                user.setCivilId(updateRequest.getCivilId());
            }
        }

        if (updateRequest.getFirstName() != null) user.setFirstName(updateRequest.getFirstName());
        if (updateRequest.getLastName() != null) user.setLastName(updateRequest.getLastName());
        if (updateRequest.getPhoneNumber() != null) user.setPhoneNumber(updateRequest.getPhoneNumber());
        if (updateRequest.getBankAccountUsername() != null) user.setBankAccountUsername(updateRequest.getBankAccountUsername());
        if (updateRequest.getSubscription() != null) user.setSubscription(updateRequest.getSubscription());
        if (updateRequest.getProfilePic() != null) user.setProfilePic(updateRequest.getProfilePic());
        if (updateRequest.getGender() != null) user.setGender(updateRequest.getGender());
        if (updateRequest.getDateOfBirth() != null) user.setDateOfBirth(updateRequest.getDateOfBirth());
        if (updateRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
        }

        userRepository.save(user);

        return new UserResponse(user);
    }

    public UserResponse connectBankAccount(Authentication authentication, String bankAccountUsername) {
        if (bankAccountUsername == null || bankAccountUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank account username is required");
        }

        UserEntity user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        // Check if user already has a bank account connected
        if (user.getBankAccountUsername() != null || user.getBankAccountNumber() != null) {
            throw new IllegalArgumentException("User already has a bank account connected. Please disconnect the current bank account first.");
        }

        // Check if bank account username is already in use
        Optional<UserEntity> existingUser = userRepository.findByBankAccountUsername(bankAccountUsername);
        if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Bank account username is already in use");
        }

        user.setBankAccountUsername(bankAccountUsername);
        user.setBankAccountNumber(generateUniqueBankAccountNumber());

        userRepository.save(user);
        return new UserResponse(user);
    }

    public UserResponse disconnectBankAccount(Authentication authentication) {
        UserEntity user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        user.setBankAccountUsername(null);
        user.setBankAccountNumber(null);

        userRepository.save(user);
        return new UserResponse(user);
    }

    public UserResponse toggleNotifications(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setNotificationEnabled(!user.getNotificationEnabled());
        userRepository.save(user);

        return new UserResponse(user);
    }

    private String generateUniqueBankAccountNumber() {
        String bankAccountNumber;
        do {
            // Generate a 16-digit bank account number
            StringBuilder number = new StringBuilder();
            for (int i = 0; i < 16; i++) {
                number.append((int) (Math.random() * 10));
            }
            bankAccountNumber = number.toString();
        } while (userRepository.findByBankAccountNumber(bankAccountNumber).isPresent());

        return bankAccountNumber;
    }
}
