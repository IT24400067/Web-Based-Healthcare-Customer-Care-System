package com.example.healthcare_customercare.repository;

import com.example.healthcare_customercare.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Find all notifications for a specific user
     */
    List<Notification> findByUserEmailOrderByCreatedAtDesc(String userEmail);

    /**
     * Find unread notifications for a specific user
     */
    List<Notification> findByUserEmailAndIsReadFalseOrderByCreatedAtDesc(String userEmail);

    /**
     * Find notifications by type for a specific user
     */
    List<Notification> findByUserEmailAndTypeOrderByCreatedAtDesc(String userEmail, String type);

    /**
     * Find notifications by priority for a specific user
     */
    List<Notification> findByUserEmailAndPriorityOrderByCreatedAtDesc(String userEmail, String priority);

    /**
     * Find notifications that are not expired
     */
    @Query("SELECT n FROM Notification n WHERE n.userEmail = :userEmail AND (n.expiresAt IS NULL OR n.expiresAt > :now) ORDER BY n.createdAt DESC")
    List<Notification> findActiveNotificationsByUser(@Param("userEmail") String userEmail, @Param("now") LocalDateTime now);

    /**
     * Count unread notifications for a specific user
     */
    long countByUserEmailAndIsReadFalse(String userEmail);

    /**
     * Find notifications related to a specific entity
     */
    List<Notification> findByRelatedEntityIdAndRelatedEntityType(Long relatedEntityId, String relatedEntityType);

    /**
     * Find notifications created after a specific date
     */
    List<Notification> findByUserEmailAndCreatedAtAfterOrderByCreatedAtDesc(String userEmail, LocalDateTime createdAt);

    /**
     * Delete expired notifications
     */
    @Query("DELETE FROM Notification n WHERE n.expiresAt IS NOT NULL AND n.expiresAt < :now")
    void deleteExpiredNotifications(@Param("now") LocalDateTime now);
}

