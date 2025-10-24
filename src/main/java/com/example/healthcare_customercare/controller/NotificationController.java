package com.example.healthcare_customercare.controller;

import com.example.healthcare_customercare.entity.Notification;
import com.example.healthcare_customercare.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for notification operations
 */
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * Get all notifications for a user
     */
    @GetMapping("/user/{userEmail}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(@PathVariable String userEmail) {
        try {
            List<Notification> notifications = notificationService.getNotificationsByUser(userEmail);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            System.err.println("Error getting notifications for user " + userEmail + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get unread notifications for a user
     */
    @GetMapping("/user/{userEmail}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotificationsByUser(@PathVariable String userEmail) {
        try {
            List<Notification> notifications = notificationService.getUnreadNotificationsByUser(userEmail);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            System.err.println("Error getting unread notifications for user " + userEmail + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get unread notification count for a user
     */
    @GetMapping("/user/{userEmail}/count")
    public ResponseEntity<Long> getUnreadNotificationCount(@PathVariable String userEmail) {
        try {
            long count = notificationService.getUnreadNotificationCount(userEmail);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            System.err.println("Error getting notification count for user " + userEmail + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Mark a notification as read
     */
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Long notificationId) {
        try {
            Notification notification = notificationService.markAsRead(notificationId);
            if (notification != null) {
                return ResponseEntity.ok(notification);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error marking notification as read: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Mark all notifications as read for a user
     */
    @PutMapping("/user/{userEmail}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable String userEmail) {
        try {
            notificationService.markAllAsRead(userEmail);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error marking all notifications as read for user " + userEmail + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Delete a notification
     */
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        try {
            notificationService.deleteNotification(notificationId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error deleting notification: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get all notifications for a user (including expired) - for debugging
     */
    @GetMapping("/user/{userEmail}/debug")
    public ResponseEntity<List<Notification>> getAllNotificationsByUser(@PathVariable String userEmail) {
        try {
            List<Notification> notifications = notificationService.getAllNotificationsByUser(userEmail);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            System.err.println("Error getting all notifications for user " + userEmail + ": " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Create a test notification (for testing purposes)
     */
    @PostMapping("/test")
    public ResponseEntity<Notification> createTestNotification(
            @RequestParam String userEmail,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam(defaultValue = "SYSTEM") String type,
            @RequestParam(defaultValue = "MEDIUM") String priority) {
        try {
            Notification notification = notificationService.createNotification(userEmail, title, message, type, priority);
            return ResponseEntity.ok(notification);
        } catch (Exception e) {
            System.err.println("Error creating test notification: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
