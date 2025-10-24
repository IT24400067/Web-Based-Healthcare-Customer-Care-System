package com.example.healthcare_customercare.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "doctor_schedules")
public class DoctorSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_schedule_id")
    private Long doctorScheduleId;
    
    @Column(name = "available_date", nullable = false)
    private LocalDate availableDate;
    
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;
    
    @Column(name = "time_period", nullable = false)
    private String timePeriod;
    
    @Column(name = "doc_id", nullable = false)
    private Long docId;
    
    @Column(name = "room_id", nullable = false)
    private Long roomId;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;
    
    // Constructors
    public DoctorSchedule() {
    }
    
    public DoctorSchedule(LocalDate availableDate, LocalTime startTime, LocalTime endTime, 
                         String timePeriod, Long docId, Long roomId) {
        this.availableDate = availableDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timePeriod = timePeriod;
        this.docId = docId;
        this.roomId = roomId;
        this.isActive = true;
        this.isAvailable = true;
    }
    
    // Getters and Setters
    public Long getDoctorScheduleId() {
        return doctorScheduleId;
    }
    
    public void setDoctorScheduleId(Long doctorScheduleId) {
        this.doctorScheduleId = doctorScheduleId;
    }
    
    public LocalDate getAvailableDate() {
        return availableDate;
    }
    
    public void setAvailableDate(LocalDate availableDate) {
        this.availableDate = availableDate;
    }
    
    public LocalTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    public String getTimePeriod() {
        return timePeriod;
    }
    
    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
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
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Boolean getIsAvailable() {
        return isAvailable;
    }
    
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    @Override
    public String toString() {
        return "DoctorSchedule{" +
                "doctorScheduleId=" + doctorScheduleId +
                ", availableDate=" + availableDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", timePeriod='" + timePeriod + '\'' +
                ", docId=" + docId +
                ", roomId=" + roomId +
                ", isActive=" + isActive +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
