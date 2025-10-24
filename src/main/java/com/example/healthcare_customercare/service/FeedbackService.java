package com.example.healthcare_customercare.service;

import com.example.healthcare_customercare.entity.Feedback;
import com.example.healthcare_customercare.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    /**
     * Save feedback to the database
     */
    public Feedback saveFeedback(String feedbackType, String message, Integer rating, String userEmail) {
        // Validate input parameters
        if (feedbackType == null || feedbackType.trim().isEmpty()) {
            throw new IllegalArgumentException("Feedback type is required");
        }
        
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("Message is required");
        }
        
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        
        if (userEmail == null || userEmail.trim().isEmpty() || !userEmail.contains("@")) {
            throw new IllegalArgumentException("Valid user email is required");
        }
        
        // Create new feedback
        Feedback feedback = new Feedback(
            feedbackType.trim(),
            message.trim(),
            rating,
            userEmail.trim()
        );
        
        // Save to database
        return feedbackRepository.save(feedback);
    }
    
    /**
     * Get all feedback
     */
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
    
    /**
     * Get feedback by ID
     */
    public Optional<Feedback> getFeedbackById(Long feedbackId) {
        return feedbackRepository.findById(feedbackId);
    }
    
    /**
     * Get feedback by user email
     */
    public List<Feedback> getFeedbackByUserEmail(String userEmail) {
        return feedbackRepository.findByUserEmail(userEmail);
    }
    
    /**
     * Get feedback by type
     */
    public List<Feedback> getFeedbackByType(String feedbackType) {
        return feedbackRepository.findByFeedbackType(feedbackType);
    }
    
    /**
     * Get feedback by rating range
     */
    public List<Feedback> getFeedbackByRatingRange(Integer minRating, Integer maxRating) {
        return feedbackRepository.findByRatingBetween(minRating, maxRating);
    }
    
    /**
     * Get feedback created between dates
     */
    public List<Feedback> getFeedbackByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return feedbackRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    /**
     * Delete feedback
     */
    public boolean deleteFeedback(Long feedbackId) {
        if (feedbackRepository.existsById(feedbackId)) {
            feedbackRepository.deleteById(feedbackId);
            return true;
        }
        return false;
    }
    
    /**
     * Get feedback count by rating
     */
    public long getFeedbackCountByRating(Integer rating) {
        return feedbackRepository.countByRating(rating);
    }
    
    /**
     * Get feedback count by type
     */
    public long getFeedbackCountByType(String feedbackType) {
        return feedbackRepository.countByFeedbackType(feedbackType);
    }
    
    /**
     * Get average rating
     */
    public Double getAverageRating() {
        return feedbackRepository.getAverageRating();
    }
    
    /**
     * Get feedback count by type
     */
    public List<Object[]> getFeedbackCountByType() {
        return feedbackRepository.getFeedbackCountByType();
    }
    
    /**
     * Get feedback count by rating
     */
    public List<Object[]> getFeedbackCountByRating() {
        return feedbackRepository.getFeedbackCountByRating();
    }
    
    /**
     * Get recent feedback (last 30 days)
     */
    public List<Feedback> getRecentFeedback() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        return feedbackRepository.findRecentFeedback(thirtyDaysAgo);
    }
}