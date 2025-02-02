package com.example.capstone.controllers;

import com.example.capstone.bo.UpdateUserRequest;
import com.example.capstone.bo.UserResponse;
import com.example.capstone.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.NoSuchElementException;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
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


}
