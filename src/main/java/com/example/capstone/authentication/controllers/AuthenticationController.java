package com.example.capstone.authentication.controllers;

import com.example.capstone.authentication.bo.*;
import com.example.capstone.authentication.bo.RegisterUserRequest;
import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.authentication.services.AuthenticationService;
import com.example.capstone.authentication.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup-user")
    public ResponseEntity<RegisterUserResponse> registerUser(@RequestBody RegisterUserRequest registerUser) {
        // Register the user
        RegisterUserResponse registeredUserResponse = authenticationService.signupUser(registerUser);

        // Extract necessary information from the registered user
        String username = registeredUserResponse.getEmail(); // Assuming RegisterUserResponse has the email field
        String role = registeredUserResponse.getRole();      // Assuming RegisterUserResponse has the role field
        String id = String.valueOf(registeredUserResponse.getId()); // Assuming RegisterUserResponse has the id field

        // Generate the JWT token
        String jwtToken = jwtService.generateToken(new org.springframework.security.core.userdetails.User(username, "", new ArrayList<>()), role, id);

        // Add the token to the response
        registeredUserResponse.setToken(jwtToken);
        registeredUserResponse.setExpiresIn(jwtService.getExpirationTime());

        // Return the response with token
        return ResponseEntity.ok(registeredUserResponse);
    }



    @PostMapping("/signup-admin")
    public ResponseEntity<RegisterAdminResponse> registerAdmin(@RequestBody RegisterAdminRequest registerAdminRequest) {
        RegisterAdminResponse registeredAdminResponse = authenticationService.signupAdmin(registerAdminRequest);

        return ResponseEntity.ok(registeredAdminResponse);
    }


    @PostMapping("/login-user")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginUser) {
        // Authenticate the user
        UserEntity authenticatedUser = authenticationService.authenticate(loginUser);

        // Extract necessary information
        String username = authenticatedUser.getEmail(); // Assuming UserEntity has a method getEmail()
        String role = authenticatedUser.getRole();      // Assuming UserEntity has a method getRole()
        String id = String.valueOf(authenticatedUser.getId()); // Assuming UserEntity has a method getId()

        // Generate the JWT token
        String jwtToken = jwtService.generateToken(new org.springframework.security.core.userdetails.User(username, "", new ArrayList<>()), role, id);

        // Prepare the login response
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setRole(role);

        return ResponseEntity.ok(loginResponse);
    }


    @PostMapping("/login-admin")
    public ResponseEntity<LoginResponse> authenticateAdmin(@RequestBody LoginRequest loginUser) {
        // Authenticate the admin
        UserEntity authenticatedAdmin = authenticationService.authenticate(loginUser);

        // Extract necessary information
        String username = authenticatedAdmin.getEmail(); // Assuming UserEntity has a method getEmail()
        String role = authenticatedAdmin.getRole();      // Assuming UserEntity has a method getRole()
        String id = String.valueOf(authenticatedAdmin.getId()); // Assuming UserEntity has a method getId()

        // Generate the JWT token
        String jwtToken = jwtService.generateToken(new org.springframework.security.core.userdetails.User(username, "", new ArrayList<>()), role, id);

        // Prepare the login response
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setRole(role);

        return ResponseEntity.ok(loginResponse);
    }


}
