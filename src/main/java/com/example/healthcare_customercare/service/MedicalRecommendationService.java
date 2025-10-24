package com.example.healthcare_customercare.service;

import com.example.healthcare_customercare.entity.MedicalRecommendation;
import com.example.healthcare_customercare.repository.MedicalRecommendationRepository;
import com.example.healthcare_customercare.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecommendationService {
    
    @Autowired
    private MedicalRecommendationRepository medicalRecommendationRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Save a new medical recommendation
     */
    public MedicalRecommendation saveMedicalRecommendation(MedicalRecommendation medicalRecommendation) {
        MedicalRecommendation savedRecommendation = medicalRecommendationRepository.save(medicalRecommendation);
        
        // Send notification to patient about new medical recommendation
        try {
            String message = "You have a new " + savedRecommendation.getRecommendationType() + " recommendation from your doctor. Please review it in your medical records.";
            notificationService.createRecommendationNotification(savedRecommendation.getPatientEmail(), message, savedRecommendation.getRecommendationId());
            System.out.println("Medical recommendation notification sent to: " + savedRecommendation.getPatientEmail());
        } catch (Exception e) {
            System.err.println("Error sending medical recommendation notification: " + e.getMessage());
        }
        
        return savedRecommendation;
    }
    
    /**
     * Create a new medical recommendation from form data
     */
    public MedicalRecommendation createMedicalRecommendation(String patientEmail, String recommendationType, 
                                                           String recommendationDetails, String priority, 
                                                           String followUpDate, String createdBy) {
        LocalDate followUp = null;
        if (followUpDate != null && !followUpDate.trim().isEmpty()) {
            followUp = LocalDate.parse(followUpDate, DateTimeFormatter.ISO_LOCAL_DATE);
        }
        
        MedicalRecommendation recommendation = new MedicalRecommendation(patientEmail, recommendationType, 
                                                                       recommendationDetails, recommendationDetails, priority, 
                                                                       followUp, createdBy);
        return saveMedicalRecommendation(recommendation);
    }
    
    /**
     * Find all recommendations for a patient
     */
    public List<MedicalRecommendation> getRecommendationsByPatient(String patientEmail) {
        return medicalRecommendationRepository.findByPatientEmail(patientEmail);
    }
    
    /**
     * Find recommendations by priority
     */
    public List<MedicalRecommendation> getRecommendationsByPriority(String priority) {
        return medicalRecommendationRepository.findByPriority(priority);
    }
    
    /**
     * Find recommendations by type
     */
    public List<MedicalRecommendation> getRecommendationsByType(String recommendationType) {
        return medicalRecommendationRepository.findByRecommendationType(recommendationType);
    }
    
    /**
     * Find recommendations created by a specific user
     */
    public List<MedicalRecommendation> getRecommendationsByCreatedBy(String createdBy) {
        return medicalRecommendationRepository.findByCreatedBy(createdBy);
    }
    
    /**
     * Get all recommendations
     */
    public List<MedicalRecommendation> getAllRecommendations() {
        return medicalRecommendationRepository.findAll();
    }
    
    /**
     * Get recommendation by ID
     */
    public Optional<MedicalRecommendation> getRecommendationById(Long recommendationId) {
        return medicalRecommendationRepository.findById(recommendationId);
    }
    
    /**
     * Count pending recommendations (recommendations with high or urgent priority)
     */
    public long countPendingRecommendations() {
        return medicalRecommendationRepository.findByPriority("high").size() + 
               medicalRecommendationRepository.findByPriority("urgent").size();
    }
    
    /**
     * Count urgent recommendations (recommendations with urgent priority)
     */
    public long countUrgentRecommendations() {
        return medicalRecommendationRepository.findByPriority("urgent").size();
    }
    
    /**
     * Update a medical recommendation
     */
    public boolean updateMedicalRecommendation(Long recommendationId, String recommendationType,
                                             String recommendationDetails, String priority, String followUpDate) {
        try {
            Optional<MedicalRecommendation> optionalRecommendation = medicalRecommendationRepository.findById(recommendationId);
            if (optionalRecommendation.isPresent()) {
                MedicalRecommendation recommendation = optionalRecommendation.get();
                recommendation.setRecommendationType(recommendationType);
                recommendation.setRecommendationDetails(recommendationDetails);
                recommendation.setRecommendationText(recommendationDetails); // Update both fields
                recommendation.setPriority(priority);
                
                if (followUpDate != null && !followUpDate.trim().isEmpty()) {
                    recommendation.setFollowUpDate(LocalDate.parse(followUpDate, DateTimeFormatter.ISO_LOCAL_DATE));
                } else {
                    recommendation.setFollowUpDate(null);
                }
                
                medicalRecommendationRepository.save(recommendation);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error updating medical recommendation: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a medical recommendation
     */
    public boolean deleteMedicalRecommendation(Long recommendationId) {
        try {
            if (medicalRecommendationRepository.existsById(recommendationId)) {
                medicalRecommendationRepository.deleteById(recommendationId);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting medical recommendation: " + e.getMessage());
            return false;
        }
    }
}
