package com.example.healthcare_customercare.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medical_reports")
public class MedicalReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "patient_email", nullable = false)
    private String patientEmail;

    @Column(name = "report_type", nullable = false)
    private String reportType;

    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @Column(name = "report_description", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String reportDescription;

    @Column(name = "priority", nullable = false)
    private String priority;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by")
    private String createdBy;

    // Default constructor
    public MedicalReport() {
        this.createdAt = LocalDateTime.now();
    }

    // Constructor with required fields
    public MedicalReport(String patientEmail, String reportType, LocalDate reportDate,
                        String reportDescription, String priority, String createdBy) {
        this.patientEmail = patientEmail;
        this.reportType = reportType;
        this.reportDate = reportDate;
        this.reportDescription = reportDescription;
        this.priority = priority;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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
        return "MedicalReport{" +
                "reportId=" + reportId +
                ", patientEmail='" + patientEmail + '\'' +
                ", reportType='" + reportType + '\'' +
                ", reportDate=" + reportDate +
                ", priority='" + priority + '\'' +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
