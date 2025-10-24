package com.example.healthcare_customercare.observer;

import com.example.healthcare_customercare.entity.Notification;
import org.springframework.stereotype.Component;

/**
 * Logging observer for monitoring and debugging notification events
 */
@Component
public class LoggingNotificationObserver implements NotificationObserver {

    @Override
    public void onNotificationCreated(Notification notification) {
        System.out.println("[NOTIFICATION CREATED] " + 
                          "User: " + notification.getUserEmail() + 
                          ", Type: " + notification.getType() + 
                          ", Priority: " + notification.getPriority() + 
                          ", Title: " + notification.getTitle());
    }

    @Override
    public void onNotificationUpdated(Notification notification) {
        System.out.println("[NOTIFICATION UPDATED] " + 
                          "ID: " + notification.getNotificationId() + 
                          ", User: " + notification.getUserEmail() + 
                          ", Read: " + notification.getIsRead());
    }

    @Override
    public void onNotificationDeleted(Long notificationId) {
        System.out.println("[NOTIFICATION DELETED] ID: " + notificationId);
    }
}

