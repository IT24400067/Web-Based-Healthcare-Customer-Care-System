package com.example.healthcare_customercare.observer;

import com.example.healthcare_customercare.entity.Notification;

/**
 * Observer interface for the Observer design pattern
 * Implementations will be notified when notifications are created
 */
public interface NotificationObserver {
    
    /**
     * Called when a new notification is created
     * @param notification The notification that was created
     */
    void onNotificationCreated(Notification notification);
    
    /**
     * Called when a notification is updated (e.g., marked as read)
     * @param notification The notification that was updated
     */
    void onNotificationUpdated(Notification notification);
    
    /**
     * Called when a notification is deleted
     * @param notificationId The ID of the notification that was deleted
     */
    void onNotificationDeleted(Long notificationId);
}

