package com.example.capstone.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.bo.TokenRequest;
import com.example.capstone.entities.NotificationEntity;
import com.example.capstone.services.NotificationService;
import com.example.capstone.services.UserService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PostMapping("/token")
    public ResponseEntity<Void> saveToken(@RequestBody TokenRequest request) {
        UserEntity user = getAuthenticatedUser();
        user.setNotificationToken(request.getToken());
        notificationService.saveNotificationToken(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<NotificationEntity>> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UserEntity user = getAuthenticatedUser();
        return ResponseEntity.ok(
            notificationService.getUserNotifications(user, PageRequest.of(page, size))
        );
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        UserEntity user = getAuthenticatedUser();
        notificationService.markAsRead(id, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount() {
        UserEntity user = getAuthenticatedUser();
        return ResponseEntity.ok(notificationService.getUnreadCount(user));
    }

    @PutMapping("/toggle-notifications")
    public ResponseEntity<Boolean> toggleNotifications() {
        UserEntity user = getAuthenticatedUser();
        user.setNotificationEnabled(!user.getNotificationEnabled());
        userService.updateUser(user.getId(), null); // Using null as we're not updating other fields
        return ResponseEntity.ok(user.getNotificationEnabled());
    }

    private UserEntity getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}