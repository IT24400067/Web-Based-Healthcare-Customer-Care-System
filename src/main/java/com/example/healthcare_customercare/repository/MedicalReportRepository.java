package com.example.healthcare_customercare.repository;

import com.example.healthcare_customercare.entity.MedicalReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalReportRepository extends JpaRepository<MedicalReport, Long> {
    
    // Find all reports for a specific patient
    List<MedicalReport> findByPatientEmail(String patientEmail);
    
    // Find reports by priority
    List<MedicalReport> findByPriority(String priority);
    
    // Find reports by type
    List<MedicalReport> findByReportType(String reportType);
    
    // Find reports created by a specific user
    List<MedicalReport> findByCreatedBy(String createdBy);
}
