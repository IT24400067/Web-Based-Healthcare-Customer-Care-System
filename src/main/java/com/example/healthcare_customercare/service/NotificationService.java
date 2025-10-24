package com.example.healthcare_customercare.service;

import com.example.healthcare_customercare.entity.Notification;
import com.example.healthcare_customercare.observer.NotificationObserver;
import com.example.healthcare_customercare.observer.NotificationSubject;
import com.example.healthcare_customercare.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationService implements NotificationSubject {

    @Autowired
    private NotificationRepository notificationRepository;

    // List of observers
    private final List<NotificationObserver> observers = new ArrayList<>();

    /**
     * Create a new notification and notify all observers
     */
    public Notification createNotification(String userEmail, String title, String message, 
                                         String type, String priority) {
        Notification notification = new Notification(userEmail, title, message, type, priority);
        Notification savedNotification = notificationRepository.save(notification);
        
        // Notify all observers
        notifyObservers(savedNotification);
        
        return savedNotification;
    }

    /**
     * Create a notification with related entity information
     */
    public Notification createNotification(String userEmail, String title, String message, 
                                         String type, String priority, Long relatedEntityId, 
                                         String relatedEntityType) {
        Notification notification = new Notification(userEmail, title, message, type, priority, 
                                                   relatedEntityId, relatedEntityType);
        Notification savedNotification = notificationRepository.save(notification);
        
        // Notify all observers
        notifyObservers(savedNotification);
        
        return savedNotification;
    }

    /**
     * Create a notification with expiration date
     */
    public Notification createNotification(String userEmail, String title, String message, 
                                         String type, String priority, LocalDateTime expiresAt) {
        Notification notification = new Notification(userEmail, title, message, type, priority);
        notification.setExpiresAt(expiresAt);
        Notification savedNotification = notificationRepository.save(notification);
        
        // Notify all observers
        notifyObservers(savedNotification);
        
        return savedNotification;
    }

    /**
     * Mark a notification as read
     */
    public Notification markAsRead(Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setIsRead(true);
            Notification updatedNotification = notificationRepository.save(notification);
            
            // Notify observers of update
            notifyObserversOfUpdate(updatedNotification);
            
            return updatedNotification;
        }
        return null;
    }

    /**
     * Mark all notifications as read for a user
     */
    public void markAllAsRead(String userEmail) {
        List<Notification> unreadNotifications = notificationRepository.findByUserEmailAndIsReadFalseOrderByCreatedAtDesc(userEmail);
        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
            notifyObserversOfUpdate(notification);
        }
    }

    /**
     * Delete a notification
     */
    public void deleteNotification(Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
        if (notificationOpt.isPresent()) {
            notificationRepository.deleteById(notificationId);
            notifyObserversOfDeletion(notificationId);
        }
    }

    /**
     * Get all notifications for a user
     */
    public List<Notification> getNotificationsByUser(String userEmail) {
        return notificationRepository.findActiveNotificationsByUser(userEmail, LocalDateTime.now());
    }

    /**
     * Get all notifications for a user (including expired) - for debugging
     */
    public List<Notification> getAllNotificationsByUser(String userEmail) {
        return notificationRepository.findByUserEmailOrderByCreatedAtDesc(userEmail);
    }

    /**
     * Get unread notifications for a user
     */
    public List<Notification> getUnreadNotificationsByUser(String userEmail) {
        return notificationRepository.findByUserEmailAndIsReadFalseOrderByCreatedAtDesc(userEmail);
    }

    /**
     * Get notification count for a user
     */
    public long getUnreadNotificationCount(String userEmail) {
        return notificationRepository.countByUserEmailAndIsReadFalse(userEmail);
    }

    /**
     * Clean up expired notifications
     */
    public void cleanupExpiredNotifications() {
        notificationRepository.deleteExpiredNotifications(LocalDateTime.now());
    }

    // Observer pattern implementation
    @Override
    public void registerObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Notification notification) {
        for (NotificationObserver observer : observers) {
            try {
                observer.onNotificationCreated(notification);
            } catch (Exception e) {
                // Log error but don't stop other observers
                System.err.println("Error notifying observer: " + e.getMessage());
            }
        }
    }

    @Override
    public void notifyObserversOfUpdate(Notification notification) {
        for (NotificationObserver observer : observers) {
            try {
                observer.onNotificationUpdated(notification);
            } catch (Exception e) {
                // Log error but don't stop other observers
                System.err.println("Error notifying observer of update: " + e.getMessage());
            }
        }
    }

    @Override
    public void notifyObserversOfDeletion(Long notificationId) {
        for (NotificationObserver observer : observers) {
            try {
                observer.onNotificationDeleted(notificationId);
            } catch (Exception e) {
                // Log error but don't stop other observers
                System.err.println("Error notifying observer of deletion: " + e.getMessage());
            }
        }
    }

    /**
     * Convenience methods for common notification types
     */
    
    public Notification createAppointmentNotification(String userEmail, String message, Long appointmentId) {
        return createNotification(userEmail, "Appointment Update", message, 
                                "APPOINTMENT", "MEDIUM", appointmentId, "APPOINTMENT");
    }

    public Notification createMedicalReportNotification(String userEmail, String message, Long reportId) {
        return createNotification(userEmail, "Medical Report Available", message, 
                                "MEDICAL_REPORT", "HIGH", reportId, "MEDICAL_REPORT");
    }

    public Notification createRecommendationNotification(String userEmail, String message, Long recommendationId) {
        return createNotification(userEmail, "New Medical Recommendation", message, 
                                "RECOMMENDATION", "HIGH", recommendationId, "RECOMMENDATION");
    }

    public Notification createSupportTicketNotification(String userEmail, String message, Long ticketId) {
        return createNotification(userEmail, "Support Ticket Update", message, 
                                "SUPPORT_TICKET", "MEDIUM", ticketId, "SUPPORT_TICKET");
    }

    public Notification createSystemNotification(String userEmail, String title, String message) {
        return createNotification(userEmail, title, message, "SYSTEM", "LOW");
    }
}
