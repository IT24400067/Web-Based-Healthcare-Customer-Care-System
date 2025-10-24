package com.example.healthcare_customercare.repository;

import com.example.healthcare_customercare.entity.MedicalRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecommendationRepository extends JpaRepository<MedicalRecommendation, Long> {
    
    // Find all recommendations for a specific patient using custom query
    @Query("SELECT mr FROM MedicalRecommendation mr WHERE mr.patientEmail = :email")
    List<MedicalRecommendation> findByPatientEmail(@Param("email") String patientEmail);
    
    // Find recommendations by priority
    List<MedicalRecommendation> findByPriority(String priority);
    
    // Find recommendations by type
    List<MedicalRecommendation> findByRecommendationType(String recommendationType);
    
    // Find recommendations created by a specific user
    List<MedicalRecommendation> findByCreatedBy(String createdBy);
}
