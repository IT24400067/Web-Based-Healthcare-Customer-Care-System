package com.example.healthcare_customercare.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedback")
public class Feedback {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackId;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "feedback_type", nullable = false)
    private String feedbackType;
    
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "rating", nullable = false)
    private Integer rating;
    
    @Column(name = "user_email", nullable = false)
    private String userEmail;
    
    // Default constructor
    public Feedback() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructor with required fields
    public Feedback(String feedbackType, String message, Integer rating, String userEmail) {
        this.feedbackType = feedbackType;
        this.message = message;
        this.rating = rating;
        this.userEmail = userEmail;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getFeedbackId() {
        return feedbackId;
    }
    
    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getFeedbackType() {
        return feedbackType;
    }
    
    public void setFeedbackType(String feedbackType) {
        this.feedbackType = feedbackType;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    @Override
    public String toString() {
        return "Feedback{" +
                "feedbackId=" + feedbackId +
                ", createdAt=" + createdAt +
                ", feedbackType='" + feedbackType + '\'' +
                ", message='" + message + '\'' +
                ", rating=" + rating +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}