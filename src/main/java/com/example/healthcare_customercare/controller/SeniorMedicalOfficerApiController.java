package com.example.healthcare_customercare.controller;

import com.example.healthcare_customercare.entity.User;
import com.example.healthcare_customercare.service.UserService;
import com.example.healthcare_customercare.service.AppointmentService;
import com.example.healthcare_customercare.service.MedicalReportService;
import com.example.healthcare_customercare.service.MedicalRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SeniorMedicalOfficerApiController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private MedicalReportService medicalReportService;
    
    @Autowired
    private MedicalRecommendationService medicalRecommendationService;

    // Search patient by email
    @GetMapping("/patient/search")
    public ResponseEntity<Map<String, Object>> searchPatient(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> user = userService.findUserByEmail(email);
            
            if (user.isPresent()) {
                User patient = user.get();
                Map<String, Object> patientData = new HashMap<>();
                patientData.put("email", patient.getEmail());
                patientData.put("firstName", patient.getFirstName());
                patientData.put("lastName", patient.getLastName());
                patientData.put("fullName", patient.getFullName());
                patientData.put("phoneNumber", patient.getPhoneNumber());
                patientData.put("role", patient.getRole());
                patientData.put("registrationDate", LocalDate.now().minusDays(30)); // Mock registration date
                
                response.put("success", true);
                response.put("patient", patientData);
            } else {
                response.put("success", false);
                response.put("message", "Patient not found");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching for patient: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    // Save medical report
    @PostMapping("/medical-report/save")
    public ResponseEntity<Map<String, Object>> saveMedicalReport(@RequestBody Map<String, String> reportData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
                // Extract data from request
                String patientEmail = reportData.get("patientEmail");
                String reportType = reportData.get("reportType");
                String reportDate = reportData.get("reportDate");
                String reportDescription = reportData.get("reportDescription");
                String priority = reportData.get("priority");
            
            // Validate required fields
            if (patientEmail == null || patientEmail.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Patient email is required");
                return ResponseEntity.ok(response);
            }
            
            if (reportType == null || reportType.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Report type is required");
                return ResponseEntity.ok(response);
            }
            
                if (reportDescription == null || reportDescription.trim().isEmpty()) {
                    response.put("success", false);
                    response.put("message", "Report description is required");
                    return ResponseEntity.ok(response);
                }
            
            // Create and save the medical report
            // For now, we'll use "SMO" as the createdBy field
            medicalReportService.createMedicalReport(patientEmail, reportType, reportDate, 
                                                   reportDescription, priority, "SMO");
            
            response.put("success", true);
            response.put("message", "Medical report saved successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error saving medical report: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    // Save medical recommendation
    @PostMapping("/medical-recommendation/save")
    public ResponseEntity<Map<String, Object>> saveMedicalRecommendation(@RequestBody Map<String, String> recommendationData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Extract data from request
            String patientEmail = recommendationData.get("patientEmail");
            String recommendationType = recommendationData.get("recommendationType");
            String recommendationDetails = recommendationData.get("recommendationDetails");
            String priority = recommendationData.get("priority");
            String followUpDate = recommendationData.get("followUpDate");
            
            // Validate required fields
            if (patientEmail == null || patientEmail.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Patient email is required");
                return ResponseEntity.ok(response);
            }
            
            if (recommendationType == null || recommendationType.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Recommendation type is required");
                return ResponseEntity.ok(response);
            }
            
            if (recommendationDetails == null || recommendationDetails.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Recommendation details are required");
                return ResponseEntity.ok(response);
            }
            
            // Create and save the medical recommendation
            // For now, we'll use "SMO" as the createdBy field
            medicalRecommendationService.createMedicalRecommendation(patientEmail, recommendationType, 
                                                                    recommendationDetails, priority, 
                                                                    followUpDate, "SMO");
            
            response.put("success", true);
            response.put("message", "Medical recommendation saved successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error saving medical recommendation: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    // Get medical reports for a patient
    @GetMapping("/medical-reports/patient")
    public ResponseEntity<Map<String, Object>> getMedicalReportsForPatient(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<com.example.healthcare_customercare.entity.MedicalReport> reports = 
                medicalReportService.getReportsByPatient(email);
            
            List<Map<String, Object>> reportData = new ArrayList<>();
            for (com.example.healthcare_customercare.entity.MedicalReport report : reports) {
                Map<String, Object> reportInfo = new HashMap<>();
                reportInfo.put("reportId", report.getReportId());
                reportInfo.put("reportType", report.getReportType());
                reportInfo.put("reportDate", report.getReportDate().toString());
                reportInfo.put("reportDescription", report.getReportDescription());
                reportInfo.put("priority", report.getPriority());
                reportInfo.put("createdAt", report.getCreatedAt().toString());
                reportInfo.put("createdBy", report.getCreatedBy());
                reportData.add(reportInfo);
            }
            
            response.put("success", true);
            response.put("reports", reportData);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error loading medical reports: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    // Get medical recommendations for a patient
    @GetMapping("/medical-recommendations/patient")
    public ResponseEntity<Map<String, Object>> getMedicalRecommendationsForPatient(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<com.example.healthcare_customercare.entity.MedicalRecommendation> recommendations = 
                medicalRecommendationService.getRecommendationsByPatient(email);
            
            List<Map<String, Object>> recommendationData = new ArrayList<>();
            for (com.example.healthcare_customercare.entity.MedicalRecommendation recommendation : recommendations) {
                Map<String, Object> recommendationInfo = new HashMap<>();
                recommendationInfo.put("recommendationId", recommendation.getRecommendationId());
                recommendationInfo.put("recommendationType", recommendation.getRecommendationType());
                recommendationInfo.put("recommendationDetails", recommendation.getRecommendationDetails());
                recommendationInfo.put("priority", recommendation.getPriority());
                recommendationInfo.put("followUpDate", recommendation.getFollowUpDate() != null ? recommendation.getFollowUpDate().toString() : null);
                recommendationInfo.put("createdAt", recommendation.getCreatedAt().toString());
                recommendationInfo.put("createdBy", recommendation.getCreatedBy());
                recommendationData.add(recommendationInfo);
            }
            
            response.put("success", true);
            response.put("recommendations", recommendationData);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error loading medical recommendations: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    // Update medical report
    @PutMapping("/medical-report/update")
    public ResponseEntity<Map<String, Object>> updateMedicalReport(@RequestBody Map<String, Object> reportData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long reportId = Long.valueOf(reportData.get("reportId").toString());
            String reportType = reportData.get("reportType").toString();
            String reportDate = reportData.get("reportDate").toString();
            String reportDescription = reportData.get("reportDescription").toString();
            String priority = reportData.get("priority").toString();
            
            // Update the medical report
            boolean updated = medicalReportService.updateMedicalReport(reportId, reportType, reportDate, 
                                                                      reportDescription, priority);
            
            if (updated) {
                response.put("success", true);
                response.put("message", "Medical report updated successfully");
            } else {
                response.put("success", false);
                response.put("message", "Medical report not found or could not be updated");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating medical report: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    // Delete medical report
    @DeleteMapping("/medical-report/delete")
    public ResponseEntity<Map<String, Object>> deleteMedicalReport(@RequestParam Long reportId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean deleted = medicalReportService.deleteMedicalReport(reportId);
            
            if (deleted) {
                response.put("success", true);
                response.put("message", "Medical report deleted successfully");
            } else {
                response.put("success", false);
                response.put("message", "Medical report not found or could not be deleted");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting medical report: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    // Update medical recommendation
    @PutMapping("/medical-recommendation/update")
    public ResponseEntity<Map<String, Object>> updateMedicalRecommendation(@RequestBody Map<String, Object> recommendationData) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long recommendationId = Long.valueOf(recommendationData.get("recommendationId").toString());
            String recommendationType = recommendationData.get("recommendationType").toString();
            String recommendationDetails = recommendationData.get("recommendationDetails").toString();
            String priority = recommendationData.get("priority").toString();
            String followUpDate = recommendationData.get("followUpDate") != null ? 
                                 recommendationData.get("followUpDate").toString() : null;
            
            // Update the medical recommendation
            boolean updated = medicalRecommendationService.updateMedicalRecommendation(recommendationId, 
                                                                                     recommendationType, 
                                                                                     recommendationDetails, 
                                                                                     priority, followUpDate);
            
            if (updated) {
                response.put("success", true);
                response.put("message", "Medical recommendation updated successfully");
            } else {
                response.put("success", false);
                response.put("message", "Medical recommendation not found or could not be updated");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating medical recommendation: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    // Delete medical recommendation
    @DeleteMapping("/medical-recommendation/delete")
    public ResponseEntity<Map<String, Object>> deleteMedicalRecommendation(@RequestParam Long recommendationId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean deleted = medicalRecommendationService.deleteMedicalRecommendation(recommendationId);
            
            if (deleted) {
                response.put("success", true);
                response.put("message", "Medical recommendation deleted successfully");
            } else {
                response.put("success", false);
                response.put("message", "Medical recommendation not found or could not be deleted");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting medical recommendation: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }

    // Get dashboard metrics for SMO
    @GetMapping("/smo/dashboard-metrics")
    public ResponseEntity<Map<String, Object>> getDashboardMetrics() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> metrics = new HashMap<>();
            
            // Get total patients count (users with CUSTOMER role)
            long totalPatients = userService.findUsersByRole("CUSTOMER").size();
            metrics.put("totalPatients", totalPatients);
            
            // Get today's appointments count
            LocalDate today = LocalDate.now();
            int todaysAppointments = appointmentService.getAppointmentsByDate(today).size();
            metrics.put("todaysAppointments", todaysAppointments);
            
            // Get actual pending reports count from database
            long pendingReports = medicalReportService.countPendingReports();
            metrics.put("pendingReports", pendingReports);
            
            // Get actual urgent cases count from database
            long urgentCases = medicalReportService.countUrgentCases();
            metrics.put("urgentCases", urgentCases);
            
            response.put("success", true);
            response.put("metrics", metrics);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error loading dashboard metrics: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}
