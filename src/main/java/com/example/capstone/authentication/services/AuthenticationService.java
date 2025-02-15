package com.example.capstone.authentication.services;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.capstone.authentication.bo.LoginRequest;
import com.example.capstone.authentication.bo.RegisterAdminRequest;
import com.example.capstone.authentication.bo.RegisterAdminResponse;
import com.example.capstone.authentication.bo.RegisterUserRequest;
import com.example.capstone.authentication.bo.RegisterUserResponse;
import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.authentication.repositories.UserRepository;
import com.example.capstone.entities.PlanEntity;
import com.example.capstone.repositories.PlanRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final PlanRepository planRepository;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            PlanRepository planRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.planRepository = planRepository;
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
        user.setBankAccountNumber(input.getBankAccountNumber());
        user.setGender(input.getGender());
        user.setDateOfBirth(input.getDateOfBirth());
        user.setProfilePic(input.getProfilePic());
        user.setRole("ROLE_USER");
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setLastSpendReset(LocalDateTime.now());
        user.setCurrentMonthlySpend(0.0);
        user.setCurrentDailySpend(0.0);
        user.setCurrentMonthCardIssuance(0);

        // Set plan-related fields
        LocalDateTime now = LocalDateTime.now();
        user.setPlan("BASIC"); // Default to BASIC plan
        user.setPlanStartDate(now);
        user.setPlanEndDate(null); // BASIC plan has no end date
        user.setAutoRenewal(false); // BASIC plan has no auto-renewal

        // Set limits based on BASIC plan
        PlanEntity basicPlan = planRepository.findByName("BASIC")
                .orElseThrow(() -> new RuntimeException("Basic plan not found"));
        user.setMonthlySpendLimit(basicPlan.getMonthlySpendLimit());
        user.setDailySpendLimit(basicPlan.getDailySpendLimit());
        user.setMonthlyCardIssuanceLimit(basicPlan.getMonthlyCardIssuanceLimit());

        UserEntity savedUser = userRepository.save(user);

        RegisterUserResponse response = new RegisterUserResponse();
        response.setId(savedUser.getId());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setEmail(savedUser.getEmail());
        response.setCivilId(savedUser.getCivilId());
        response.setPhoneNumber(savedUser.getPhoneNumber());
        response.setBankAccountUsername(savedUser.getBankAccountUsername());
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
                        input.getPassword()));

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }
}
