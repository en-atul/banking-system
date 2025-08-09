package com.proj.notificationservice.controller;

import com.proj.notificationservice.model.Notification;
import com.proj.notificationservice.repository.NotificationRepository;
import com.proj.notificationservice.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserNotifications(@PathVariable Long userId) {
        try {
            List<Notification> notifications = notificationRepository.findByUserIdOrderBySentAtDesc(userId);
            return ResponseEntity.ok(new ApiResponse("Notifications retrieved successfully", notifications));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Error retrieving notifications: " + e.getMessage(), null));
        }
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<ApiResponse> getUnreadNotifications(@PathVariable Long userId) {
        try {
            List<Notification> notifications = notificationRepository.findByUserIdAndReadOrderBySentAtDesc(userId, false);
            return ResponseEntity.ok(new ApiResponse("Unread notifications retrieved successfully", notifications));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Error retrieving unread notifications: " + e.getMessage(), null));
        }
    }

    @GetMapping("/user/{userId}/count/unread")
    public ResponseEntity<ApiResponse> getUnreadNotificationCount(@PathVariable Long userId) {
        try {
            long count = notificationRepository.countByUserIdAndRead(userId, false);
            return ResponseEntity.ok(new ApiResponse("Unread notification count retrieved successfully", count));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Error retrieving unread notification count: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<ApiResponse> markAsRead(@PathVariable String notificationId) {
        try {
            Notification notification = notificationRepository.findById(notificationId).orElse(null);
            if (notification != null) {
                notification.setRead(true);
                Notification updatedNotification = notificationRepository.save(notification);
                return ResponseEntity.ok(new ApiResponse("Notification marked as read", updatedNotification));
            } else {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Notification not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("Error marking notification as read: " + e.getMessage(), null));
        }
    }
}
