package com.example.capstone.services;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.authentication.repositories.UserRepository;
import com.example.capstone.bo.AdminResponse;
import com.example.capstone.bo.UpdateAdminRequest;
import com.example.capstone.bo.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private UserRepository userRepository = null;

    public  AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AdminResponse getAdminById(Long admin_id) {
        UserEntity admin = userRepository.findById(admin_id)
                .filter(user -> "ROLE_ADMIN".equals(user.getRole()))
                .orElseThrow(() -> new NoSuchElementException("Admin with ID " + admin_id + " not found"));
        return new AdminResponse(admin);
    }

    public List<AdminResponse> getAllAdmins() {
        List<UserEntity> admins = userRepository.findAll();
        return admins.stream()
                .filter(user -> "ROLE_ADMIN".equals(user.getRole()))
                .map(AdminResponse::new)
                .collect(Collectors.toList());
    }

    public AdminResponse updateAdmin(Long adminId, UpdateAdminRequest updateRequest) {
        UserEntity admin = userRepository.findById(adminId)
                .filter(user -> "ROLE_ADMIN".equals(user.getRole()))
                .orElseThrow(() -> new NoSuchElementException("Admin with ID " + adminId + " not found"));

        if (updateRequest.getFirstName() != null) admin.setFirstName(updateRequest.getFirstName());
        if (updateRequest.getLastName() != null) admin.setLastName(updateRequest.getLastName());
        if (updateRequest.getEmail() != null) admin.setEmail(updateRequest.getEmail());
        if (updateRequest.getPhoneNumber() != null) admin.setPhoneNumber(updateRequest.getPhoneNumber());
        if (updateRequest.getPermission() != null) admin.setPermission(updateRequest.getPermission());
        if (updateRequest.getDepartment() != null) admin.setDepartment(updateRequest.getDepartment());
        if (updateRequest.getBankAccountUsername() != null) admin.setBankAccountUsername(updateRequest.getBankAccountUsername());
        if (updateRequest.getProfilePic() != null) admin.setProfilePic(updateRequest.getProfilePic());
        if (updateRequest.getGender() != null) admin.setGender(updateRequest.getGender());
        if (updateRequest.getDateOfBirth() != null) admin.setDateOfBirth(updateRequest.getDateOfBirth());

        userRepository.save(admin);

        return new AdminResponse(admin);
    }

    public void deleteAdminById(Long adminId) {
        if (!userRepository.existsById(adminId)) {
            throw new NoSuchElementException("Admin with ID " + adminId + " not found");
        }
        userRepository.deleteById(adminId);
    }




}
