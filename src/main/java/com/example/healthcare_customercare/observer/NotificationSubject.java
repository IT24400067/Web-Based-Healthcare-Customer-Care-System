package com.example.healthcare_customercare.observer;

import com.example.healthcare_customercare.entity.Notification;

/**
 * Subject interface for the Observer design pattern
 * Manages observers and notifies them of notification events
 */
public interface NotificationSubject {
    
    /**
     * Register an observer to receive notifications
     * @param observer The observer to register
     */
    void registerObserver(NotificationObserver observer);
    
    /**
     * Remove an observer from receiving notifications
     * @param observer The observer to remove
     */
    void removeObserver(NotificationObserver observer);
    
    /**
     * Notify all registered observers about a new notification
     * @param notification The notification that was created
     */
    void notifyObservers(Notification notification);
    
    /**
     * Notify all registered observers about a notification update
     * @param notification The notification that was updated
     */
    void notifyObserversOfUpdate(Notification notification);
    
    /**
     * Notify all registered observers about a notification deletion
     * @param notificationId The ID of the notification that was deleted
     */
    void notifyObserversOfDeletion(Long notificationId);
}

