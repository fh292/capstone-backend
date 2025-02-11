package com.example.capstone.services;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.capstone.authentication.entities.UserEntity;
import com.example.capstone.authentication.repositories.UserRepository;
import com.example.capstone.entities.NotificationEntity;
import com.example.capstone.repositories.NotificationRepository;

@Service
public class NotificationService {
    private static final String EXPO_PUSH_API = "https://exp.host/--/api/v2/push/send";

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    public NotificationService(
            SimpMessagingTemplate messagingTemplate,
            NotificationRepository notificationRepository,
            UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
    }

    public void saveNotificationToken(UserEntity user) {
        userRepository.save(user);
    }

    public void sendNotification(UserEntity user, String title, String body, Map<String, Object> data) {
        // 1. Save to database
        NotificationEntity notification = new NotificationEntity();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setBody(body);
        notification.setData(data);
        notificationRepository.save(notification);

        // 2. Send via WebSocket if user is connected
        messagingTemplate.convertAndSendToUser(
            user.getId().toString(),
            "/queue/notifications",
            notification
        );

        // 3. Send push notification if token exists and notifications are enabled
        if (user.getNotificationToken() != null && user.getNotificationEnabled()) {
            sendPushNotification(user.getNotificationToken(), notification);
        }
    }

    public Page<NotificationEntity> getUserNotifications(UserEntity user, Pageable pageable) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
    }

    public void markAsRead(Long notificationId, UserEntity user) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not authorized to access this notification");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public long getUnreadCount(UserEntity user) {
        return notificationRepository.countByUserAndReadFalse(user);
    }

    private void sendPushNotification(String token, NotificationEntity notification) {
        Map<String, Object> request = Map.of(
            "to", token,
            "title", notification.getTitle(),
            "body", notification.getBody(),
            "data", notification.getData() != null ? notification.getData() : Map.of(),
            "sound", "default"
        );

        restTemplate.postForEntity(EXPO_PUSH_API, request, Void.class);
    }
}