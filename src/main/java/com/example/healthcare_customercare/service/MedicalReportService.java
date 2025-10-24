package com.example.healthcare_customercare.service;

import com.example.healthcare_customercare.entity.MedicalReport;
import com.example.healthcare_customercare.repository.MedicalReportRepository;
import com.example.healthcare_customercare.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalReportService {
    
    @Autowired
    private MedicalReportRepository medicalReportRepository;
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Save a new medical report
     */
    public MedicalReport saveMedicalReport(MedicalReport medicalReport) {
        MedicalReport savedReport = medicalReportRepository.save(medicalReport);
        
        // Send notification to patient about new medical report
        try {
            String message = "Your " + savedReport.getReportType() + " report is now available. Please check your medical records.";
            notificationService.createMedicalReportNotification(savedReport.getPatientEmail(), message, savedReport.getReportId());
            System.out.println("Medical report notification sent to: " + savedReport.getPatientEmail());
        } catch (Exception e) {
            System.err.println("Error sending medical report notification: " + e.getMessage());
        }
        
        return savedReport;
    }
    
    /**
     * Create a new medical report from form data
     */
    public MedicalReport createMedicalReport(String patientEmail, String reportType,
                                           String reportDate, String reportDescription,
                                           String priority, String createdBy) {
        LocalDate date = LocalDate.parse(reportDate, DateTimeFormatter.ISO_LOCAL_DATE);

        MedicalReport report = new MedicalReport(patientEmail, reportType, date,
                                               reportDescription, priority, createdBy);
        return saveMedicalReport(report);
    }
    
    /**
     * Find all reports for a patient
     */
    public List<MedicalReport> getReportsByPatient(String patientEmail) {
        return medicalReportRepository.findByPatientEmail(patientEmail);
    }
    
    /**
     * Find reports by priority
     */
    public List<MedicalReport> getReportsByPriority(String priority) {
        return medicalReportRepository.findByPriority(priority);
    }
    
    /**
     * Find reports by type
     */
    public List<MedicalReport> getReportsByType(String reportType) {
        return medicalReportRepository.findByReportType(reportType);
    }
    
    /**
     * Find reports created by a specific user
     */
    public List<MedicalReport> getReportsByCreatedBy(String createdBy) {
        return medicalReportRepository.findByCreatedBy(createdBy);
    }
    
    /**
     * Get all reports
     */
    public List<MedicalReport> getAllReports() {
        return medicalReportRepository.findAll();
    }
    
    /**
     * Get report by ID
     */
    public Optional<MedicalReport> getReportById(Long reportId) {
        return medicalReportRepository.findById(reportId);
    }
    
    /**
     * Count pending reports (reports with high or urgent priority)
     */
    public long countPendingReports() {
        return medicalReportRepository.findByPriority("high").size() + 
               medicalReportRepository.findByPriority("urgent").size();
    }
    
    /**
     * Count urgent cases (reports with urgent priority)
     */
    public long countUrgentCases() {
        return medicalReportRepository.findByPriority("urgent").size();
    }
    
    /**
     * Update a medical report
     */
    public boolean updateMedicalReport(Long reportId, String reportType, String reportDate,
                                     String reportDescription, String priority) {
        try {
            Optional<MedicalReport> optionalReport = medicalReportRepository.findById(reportId);
            if (optionalReport.isPresent()) {
                MedicalReport report = optionalReport.get();
                report.setReportType(reportType);
                report.setReportDate(LocalDate.parse(reportDate, DateTimeFormatter.ISO_LOCAL_DATE));
                report.setReportDescription(reportDescription);
                report.setPriority(priority);
                
                medicalReportRepository.save(report);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error updating medical report: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete a medical report
     */
    public boolean deleteMedicalReport(Long reportId) {
        try {
            if (medicalReportRepository.existsById(reportId)) {
                medicalReportRepository.deleteById(reportId);
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error deleting medical report: " + e.getMessage());
            return false;
        }
    }
}
