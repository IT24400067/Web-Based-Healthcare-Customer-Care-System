package com.example.healthcare_customercare.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "appointments")
public class Appointment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "patient_name", nullable = false)
    private String patientName;
    
    @Column(name = "patient_email", nullable = false)
    private String patientEmail;
    
    @Column(name = "appointment_date")
    private LocalDate appointmentDate;
    
    @Column(name = "doc_id", nullable = false)
    private Long docId;
    
    @Column(name = "room_id", nullable = false)
    private Long roomId;
    
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;
    
    // Constructors
    public Appointment() {
    }
    
    public Appointment(String patientName, String patientEmail, LocalDate appointmentDate, 
                      Long docId, Long roomId, Long scheduleId) {
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.appointmentDate = appointmentDate;
        this.docId = docId;
        this.roomId = roomId;
        this.scheduleId = scheduleId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPatientName() {
        return patientName;
    }
    
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    
    public String getPatientEmail() {
        return patientEmail;
    }
    
    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }
    
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public Long getDocId() {
        return docId;
    }
    
    public void setDocId(Long docId) {
        this.docId = docId;
    }
    
    public Long getRoomId() {
        return roomId;
    }
    
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    
    public Long getScheduleId() {
        return scheduleId;
    }
    
    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }
    
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patientName='" + patientName + '\'' +
                ", patientEmail='" + patientEmail + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", docId=" + docId +
                ", roomId=" + roomId +
                ", scheduleId=" + scheduleId +
                '}';
    }
}
