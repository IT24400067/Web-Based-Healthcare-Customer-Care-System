package com.example.healthcare_customercare.observer;

import com.example.healthcare_customercare.entity.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * WebSocket observer for real-time notification updates
 * Sends notifications to connected clients via WebSocket
 */
@Component
public class WebSocketNotificationObserver implements NotificationObserver {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void onNotificationCreated(Notification notification) {
        // Send notification to the specific user's WebSocket channel
        String destination = "/user/" + notification.getUserEmail() + "/notifications";
        messagingTemplate.convertAndSend(destination, notification);
        
        System.out.println("WebSocket notification sent to: " + notification.getUserEmail());
    }

    @Override
    public void onNotificationUpdated(Notification notification) {
        // Send update to the specific user's WebSocket channel
        String destination = "/user/" + notification.getUserEmail() + "/notifications/update";
        messagingTemplate.convertAndSend(destination, notification);
        
        System.out.println("WebSocket notification update sent to: " + notification.getUserEmail());
    }

    @Override
    public void onNotificationDeleted(Long notificationId) {
        // Send deletion notification to all connected users
        // In a real implementation, you might want to track which users have this notification
        messagingTemplate.convertAndSend("/topic/notifications/deleted", notificationId);
        
        System.out.println("WebSocket notification deletion sent for ID: " + notificationId);
    }
}

