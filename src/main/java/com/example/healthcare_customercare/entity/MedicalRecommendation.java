package com.example.healthcare_customercare.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_recommendations")
public class MedicalRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id")
    private Long recommendationId;

    @Column(name = "patient_email", nullable = false)
    private String patientEmail;

    @Column(name = "recommendation_type", nullable = false)
    private String recommendationType;

    @Column(name = "recommendation_details", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String recommendationDetails;

    @Column(name = "recommendation_text", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String recommendationText;

    @Column(name = "priority", nullable = false)
    private String priority;

    @Column(name = "follow_up_date")
    private LocalDate followUpDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    // Default constructor
    public MedicalRecommendation() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor with required fields
    public MedicalRecommendation(String patientEmail, String recommendationType,
                                 String recommendationDetails, String recommendationText, String priority,
                                 LocalDate followUpDate, String createdBy) {
        this.patientEmail = patientEmail;
        this.recommendationType = recommendationType;
        this.recommendationDetails = recommendationDetails;
        this.recommendationText = recommendationText;
        this.priority = priority;
        this.followUpDate = followUpDate;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getRecommendationId() {
        return recommendationId;
    }
    
    public void setRecommendationId(Long recommendationId) {
        this.recommendationId = recommendationId;
    }
    
    public String getPatientEmail() {
        return patientEmail;
    }
    
    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
    
    public String getRecommendationType() {
        return recommendationType;
    }
    
    public void setRecommendationType(String recommendationType) {
        this.recommendationType = recommendationType;
    }
    
    public String getRecommendationDetails() {
        return recommendationDetails;
    }
    
    public void setRecommendationDetails(String recommendationDetails) {
        this.recommendationDetails = recommendationDetails;
    }
    
    public String getRecommendationText() {
        return recommendationText;
    }
    
    public void setRecommendationText(String recommendationText) {
        this.recommendationText = recommendationText;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public LocalDate getFollowUpDate() {
        return followUpDate;
    }
    
    public void setFollowUpDate(LocalDate followUpDate) {
        this.followUpDate = followUpDate;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    @Override
    public String toString() {
        return "MedicalRecommendation{" +
                "recommendationId=" + recommendationId +
                ", patientEmail='" + patientEmail + '\'' +
                ", recommendationType='" + recommendationType + '\'' +
                ", priority='" + priority + '\'' +
                ", followUpDate=" + followUpDate +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
