package com.example.healthcare_customercare.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Long docId;
    
    @Column(name = "doctor_name", nullable = false)
    private String doctorName;
    
    @Column(name = "specialization", nullable = false)
    private String specialization;
    
    @Column(name = "department", nullable = false)
    private String department;
    
    @Column(name = "email")
    private String email;
    
    // Constructors
    public Doctor() {
    }

    public Doctor(String doctorName, String specialization, String department, String email) {
        this.doctorName = doctorName;
        this.specialization = specialization;
        this.department = department;
        this.email = email;
    }
    
    // Getters and Setters
    public Long getDocId() {
        return docId;
    }
    
    public void setDocId(Long docId) {
        this.docId = docId;
    }
    
    public String getDoctorName() {
        return doctorName;
    }
    
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "Doctor{" +
                "docId=" + docId +
                ", doctorName='" + doctorName + '\'' +
                ", specialization='" + specialization + '\'' +
                ", department='" + department + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
