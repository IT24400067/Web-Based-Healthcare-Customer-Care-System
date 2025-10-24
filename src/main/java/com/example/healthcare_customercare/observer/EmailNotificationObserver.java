package com.example.healthcare_customercare.observer;

import com.example.healthcare_customercare.entity.Notification;
import org.springframework.stereotype.Component;

/**
 * Email notification observer
 * Sends email notifications for high-priority events
 */
@Component
public class EmailNotificationObserver implements NotificationObserver {

    @Override
    public void onNotificationCreated(Notification notification) {
        // Only send emails for high-priority notifications
        if ("HIGH".equals(notification.getPriority()) || "URGENT".equals(notification.getPriority())) {
            sendEmailNotification(notification);
        }
    }

    @Override
    public void onNotificationUpdated(Notification notification) {
        // Typically don't send emails for updates unless it's urgent
        if ("URGENT".equals(notification.getPriority())) {
            sendEmailUpdate(notification);
        }
    }

    @Override
    public void onNotificationDeleted(Long notificationId) {
        // Don't send emails for deletions
    }

    private void sendEmailNotification(Notification notification) {
        // In a real implementation, you would integrate with an email service
        // For now, we'll just log the email that would be sent
        System.out.println("=== EMAIL NOTIFICATION ===");
        System.out.println("To: " + notification.getUserEmail());
        System.out.println("Subject: " + notification.getTitle());
        System.out.println("Message: " + notification.getMessage());
        System.out.println("Priority: " + notification.getPriority());
        System.out.println("Type: " + notification.getType());
        System.out.println("=========================");
        
        // TODO: Integrate with actual email service (e.g., SendGrid, AWS SES, etc.)
        // Example:
        // emailService.sendEmail(notification.getUserEmail(), notification.getTitle(), notification.getMessage());
    }

    private void sendEmailUpdate(Notification notification) {
        System.out.println("=== EMAIL UPDATE ===");
        System.out.println("To: " + notification.getUserEmail());
        System.out.println("Subject: Update - " + notification.getTitle());
        System.out.println("Message: " + notification.getMessage());
        System.out.println("===================");
    }
}

