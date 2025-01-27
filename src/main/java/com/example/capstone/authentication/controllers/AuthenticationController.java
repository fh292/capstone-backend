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
        RegisterUserResponse registeredUserResponse = authenticationService.signupUser(registerUser);

        return ResponseEntity.ok(registeredUserResponse);
    }


    @PostMapping("/signup-admin")
    public ResponseEntity<RegisterAdminResponse> registerAdmin(@RequestBody RegisterAdminRequest registerAdminRequest) {
        RegisterAdminResponse registeredAdminResponse = authenticationService.signupAdmin(registerAdminRequest);

        return ResponseEntity.ok(registeredAdminResponse);
    }


    @PostMapping("/login-user")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginUser) {
        UserEntity authenticatedUser = authenticationService.authenticate(loginUser);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setRole("User");

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/login-admin")
    public ResponseEntity<LoginResponse> authenticateAdmin(@RequestBody LoginRequest loginUser) {
        UserEntity authenticatedAdmin = authenticationService.authenticate(loginUser);

        String jwtToken = jwtService.generateToken(authenticatedAdmin);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setRole("Admin");

        return ResponseEntity.ok(loginResponse);
    }

}
