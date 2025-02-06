package com.example.capstone.authentication.services;

import com.example.capstone.authentication.bo.LoginRequest;
import com.example.capstone.authentication.bo.RegisterAdminRequest;
import com.example.capstone.authentication.bo.RegisterAdminResponse;
import com.example.capstone.authentication.bo.RegisterUserResponse;
import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.authentication.bo.RegisterUserRequest;
import com.example.capstone.authentication.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public RegisterUserResponse signupUser(RegisterUserRequest input) {
        UserEntity user = new UserEntity();
        user.setFirstName(input.getFirstName());
        user.setLastName(input.getLastName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setCivilId(input.getCivilId());
        user.setPhoneNumber(input.getPhoneNumber());
        user.setBankAccountUsername(input.getBankAccountUsername());
        user.setSubscription("Basic");
        user.setBankAccountNumber(input.getBankAccountNumber());
        user.setGender(input.getGender());
        user.setDateOfBirth(input.getDateOfBirth());
        user.setProfilePic(input.getProfilePic());
        user.setRole("ROLE_USER");
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());

        UserEntity savedUser = userRepository.save(user);

        RegisterUserResponse response = new RegisterUserResponse();
        response.setId(savedUser.getId());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setEmail(savedUser.getEmail());
        response.setCivilId(savedUser.getCivilId());
        response.setPhoneNumber(savedUser.getPhoneNumber());
        response.setBankAccountUsername(savedUser.getBankAccountUsername());
        response.setSubscription(savedUser.getSubscription());
        response.setBankAccountNumber(savedUser.getBankAccountNumber());
        response.setGender(savedUser.getGender());
        response.setDateOfBirth(savedUser.getDateOfBirth());
        response.setProfilePic(savedUser.getProfilePic());
        response.setRole(savedUser.getRole());
        response.setCreatedAt(LocalDateTime.now());
        response.setActive(true);

        return response;
    }

    public RegisterAdminResponse signupAdmin(RegisterAdminRequest registerAdminRequest) {
        UserEntity admin = new UserEntity();

        admin.setFirstName(registerAdminRequest.getFirstName());
        admin.setLastName(registerAdminRequest.getLastName());
        admin.setEmail(registerAdminRequest.getEmail());
        admin.setPassword(passwordEncoder.encode(registerAdminRequest.getPassword()));
        admin.setPhoneNumber(registerAdminRequest.getPhoneNumber());
        admin.setPermission(registerAdminRequest.getPermission());
        admin.setDepartment(registerAdminRequest.getDepartment());
        admin.setRole("ROLE_ADMIN");

        UserEntity savedAdmin = userRepository.save(admin);

        RegisterAdminResponse adminResponse = new RegisterAdminResponse();
        adminResponse.setId(savedAdmin.getId());
        adminResponse.setFirstName(savedAdmin.getFirstName());
        adminResponse.setLastName(savedAdmin.getLastName());
        adminResponse.setEmail(savedAdmin.getEmail());
        adminResponse.setPhoneNumber(savedAdmin.getPhoneNumber());
        adminResponse.setPermission(savedAdmin.getPermission());
        adminResponse.setDepartment(savedAdmin.getDepartment());
        adminResponse.setRole(savedAdmin.getRole());

        return adminResponse;
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
