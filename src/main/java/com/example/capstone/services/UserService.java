package com.example.capstone.services;

import com.example.capstone.authentication.bo.RegisterUserResponse;
import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.authentication.repositories.UserRepository;
import com.example.capstone.bo.UpdateUserRequest;
import com.example.capstone.bo.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
@Service
public class UserService {
    private static UserRepository userRepository = null;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static UserResponse getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User with ID " + id + " not found"));

        return new UserResponse(userEntity);
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

        if (updateRequest.getFirstName() != null) user.setFirstName(updateRequest.getFirstName());
        if (updateRequest.getLastName() != null) user.setLastName(updateRequest.getLastName());
        if (updateRequest.getPhoneNumber() != null) user.setPhoneNumber(updateRequest.getPhoneNumber());
        if (updateRequest.getBankAccountUsername() != null) user.setBankAccountUsername(updateRequest.getBankAccountUsername());
        if (updateRequest.getSubscription() != null) user.setSubscription(updateRequest.getSubscription());
        if (updateRequest.getProfilePic() != null) user.setProfilePic(updateRequest.getProfilePic());
        if (updateRequest.getGender() != null) user.setGender(updateRequest.getGender());
        if (updateRequest.getDateOfBirth() != null) user.setDateOfBirth(updateRequest.getDateOfBirth());

        userRepository.save(user);

        return new UserResponse(user);
    }

    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with ID " + userId + " not found");
        }
        userRepository.deleteById(userId);
    }





}
