package com.example.capstone.controllers;

import com.example.capstone.bo.AdminResponse;
import com.example.capstone.bo.UpdateAdminRequest;
import com.example.capstone.bo.UpdateUserRequest;
import com.example.capstone.bo.UserResponse;
import com.example.capstone.services.AdminService;
import com.example.capstone.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RequestMapping("/admin")
@RestController
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/view/{admin_id}")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable Long admin_id) {
        AdminResponse response = adminService.getAdminById(admin_id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view-all")
    public ResponseEntity<List<AdminResponse>> getAllAdmins() {
        List<AdminResponse> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    @PutMapping("/update/{admin_id}")
    public ResponseEntity<AdminResponse> updateAdminProfile(@PathVariable Long admin_id,
                                                            @RequestBody UpdateAdminRequest updateRequest) {
        try {
            AdminResponse updatedAdmin = adminService.updateAdmin(admin_id, updateRequest);
            return ResponseEntity.ok(updatedAdmin);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{admin_id}")
    public ResponseEntity<String> deleteAdmin(@PathVariable Long admin_id) {
        try {
            adminService.deleteAdminById(admin_id);
            return ResponseEntity.ok("Admin deleted successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
