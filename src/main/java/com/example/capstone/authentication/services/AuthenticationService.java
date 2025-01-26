package com.example.capstone.authentication.services;

import com.example.capstone.authentication.bo.LoginRequest;
import com.example.capstone.authentication.bo.RegisterUserRequest;
import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.authentication.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity signup(RegisterUserRequest input) {
        UserEntity user = new UserEntity();
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setCivilId(input.getCivilId());
        user.setPhoneNumber(input.getPhoneNumber());
        user.setBankAccountUsername(input.getBankAccountUsername());
        user.setSubscription(input.getSubscription());
        user.setBankAccountNumber(input.getBankAccountNumber());
//        user.setCardId(input.getCard());
        user.setGender(input.getGender());
        user.setDateOfBirth(input.getDateOfBirth());
        user.setProfilePic(input.getProfilePic());
        user.setRole("ROLE_USER");

        return userRepository.save(user);
    }

    public UserEntity authenticate(LoginRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
