package com.example.healthcare_customercare.repository;

import com.example.healthcare_customercare.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    // Find feedback by user email
    List<Feedback> findByUserEmail(String userEmail);
    
    // Find feedback by feedback type
    List<Feedback> findByFeedbackType(String feedbackType);
    
    // Find feedback by rating range
    List<Feedback> findByRatingBetween(Integer minRating, Integer maxRating);
    
    // Find feedback by user email and feedback type
    List<Feedback> findByUserEmailAndFeedbackType(String userEmail, String feedbackType);
    
    // Find feedback created between dates
    List<Feedback> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Count feedback by rating
    long countByRating(Integer rating);
    
    // Count feedback by feedback type
    long countByFeedbackType(String feedbackType);
    
    // Get average rating
    @Query("SELECT AVG(f.rating) FROM Feedback f")
    Double getAverageRating();
    
    // Get feedback count by type
    @Query("SELECT f.feedbackType, COUNT(f) FROM Feedback f GROUP BY f.feedbackType")
    List<Object[]> getFeedbackCountByType();
    
    // Get feedback count by rating
    @Query("SELECT f.rating, COUNT(f) FROM Feedback f GROUP BY f.rating")
    List<Object[]> getFeedbackCountByRating();
    
    // Find recent feedback (last 30 days)
    @Query("SELECT f FROM Feedback f WHERE f.createdAt >= :startDate ORDER BY f.createdAt DESC")
    List<Feedback> findRecentFeedback(@Param("startDate") LocalDateTime startDate);
}