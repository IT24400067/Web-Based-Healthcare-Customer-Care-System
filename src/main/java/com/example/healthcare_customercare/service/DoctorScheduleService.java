package com.example.healthcare_customercare.service;

import com.example.healthcare_customercare.entity.DoctorSchedule;
import com.example.healthcare_customercare.repository.DoctorScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorScheduleService {
    
    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;
    
    /**
     * Assign doctor to room for a specific time period
     * @param docId Doctor ID
     * @param roomId Room ID
     * @param timePeriod Time period (e.g., "Morning", "Afternoon", "Evening")
     * @param availableDate Available date
     * @return Created DoctorSchedule
     */
    public DoctorSchedule assignDoctorToRoom(Long docId, Long roomId, String timePeriod, LocalDate availableDate) {
        // Validate input parameters
        if (docId == null) {
            throw new IllegalArgumentException("Doctor ID cannot be null");
        }
        if (roomId == null) {
            throw new IllegalArgumentException("Room ID cannot be null");
        }
        if (timePeriod == null || timePeriod.trim().isEmpty()) {
            throw new IllegalArgumentException("Time period cannot be null or empty");
        }
        if (availableDate == null) {
            throw new IllegalArgumentException("Available date cannot be null");
        }
        
        // Check if doctor is already assigned for this time period
        Optional<DoctorSchedule> existingDoctorSchedule = doctorScheduleRepository
                .findByDocIdAndAvailableDateAndTimePeriod(docId, availableDate, timePeriod);
        if (existingDoctorSchedule.isPresent()) {
            throw new IllegalArgumentException("Doctor is already assigned for " + timePeriod + " on " + availableDate);
        }
        
        // Check if room is already assigned for this time period
        Optional<DoctorSchedule> existingRoomSchedule = doctorScheduleRepository
                .findByRoomIdAndAvailableDateAndTimePeriod(roomId, availableDate, timePeriod);
        if (existingRoomSchedule.isPresent()) {
            throw new IllegalArgumentException("Room is already assigned for " + timePeriod + " on " + availableDate);
        }
        
        // Set start and end times based on time period
        LocalTime startTime, endTime;
        switch (timePeriod.toLowerCase()) {
            case "morning":
                startTime = LocalTime.of(8, 0);
                endTime = LocalTime.of(12, 0);
                break;
            case "afternoon":
                startTime = LocalTime.of(13, 0);
                endTime = LocalTime.of(17, 0);
                break;
            case "evening":
                startTime = LocalTime.of(18, 0);
                endTime = LocalTime.of(22, 0);
                break;
            default:
                throw new IllegalArgumentException("Invalid time period: " + timePeriod + ". Valid periods are: Morning, Afternoon, Evening");
        }
        
        try {
            // Create new schedule
            DoctorSchedule schedule = new DoctorSchedule(availableDate, startTime, endTime, timePeriod, docId, roomId);
            return doctorScheduleRepository.save(schedule);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save doctor schedule: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get schedule by ID
     * @param scheduleId Schedule ID
     * @return Optional<DoctorSchedule>
     */
    public Optional<DoctorSchedule> getScheduleById(Long scheduleId) {
        return doctorScheduleRepository.findById(scheduleId);
    }
    
    /**
     * Get all schedules
     * @return List of all schedules
     */
    public List<DoctorSchedule> getAllSchedules() {
        return doctorScheduleRepository.findAll();
    }
    
    /**
     * Get schedules by doctor ID
     * @param docId Doctor ID
     * @return List of schedules
     */
    public List<DoctorSchedule> getSchedulesByDoctor(Long docId) {
        return doctorScheduleRepository.findByDocId(docId);
    }
    
    /**
     * Get schedules by room ID
     * @param roomId Room ID
     * @return List of schedules
     */
    public List<DoctorSchedule> getSchedulesByRoom(Long roomId) {
        return doctorScheduleRepository.findByRoomId(roomId);
    }
    
    /**
     * Get schedules by date
     * @param availableDate Available date
     * @return List of schedules
     */
    public List<DoctorSchedule> getSchedulesByDate(LocalDate availableDate) {
        return doctorScheduleRepository.findByAvailableDate(availableDate);
    }
    
    /**
     * Get active schedules
     * @return List of active schedules
     */
    public List<DoctorSchedule> getActiveSchedules() {
        return doctorScheduleRepository.findByIsActiveTrue();
    }
    
    /**
     * Get available schedules
     * @return List of available schedules
     */
    public List<DoctorSchedule> getAvailableSchedules() {
        return doctorScheduleRepository.findByIsAvailableTrue();
    }
    
    /**
     * Update schedule details
     * @param scheduleId Schedule ID
     * @param roomId New room ID
     * @param availableDate New available date
     * @param timePeriod New time period
     * @param isAvailable New availability status
     * @return Updated schedule
     */
    public DoctorSchedule updateScheduleDetails(Long scheduleId, Long roomId, LocalDate availableDate, String timePeriod, Boolean isAvailable) {
        Optional<DoctorSchedule> schedule = doctorScheduleRepository.findById(scheduleId);
        if (schedule.isEmpty()) {
            throw new IllegalArgumentException("Schedule with ID " + scheduleId + " not found");
        }
        
        DoctorSchedule existingSchedule = schedule.get();
        
        // Set start and end times based on time period
        LocalTime startTime, endTime;
        switch (timePeriod.toLowerCase()) {
            case "morning":
                startTime = LocalTime.of(8, 0);
                endTime = LocalTime.of(12, 0);
                break;
            case "afternoon":
                startTime = LocalTime.of(13, 0);
                endTime = LocalTime.of(17, 0);
                break;
            case "evening":
                startTime = LocalTime.of(18, 0);
                endTime = LocalTime.of(22, 0);
                break;
            default:
                throw new IllegalArgumentException("Invalid time period: " + timePeriod);
        }
        
        // Update the schedule
        existingSchedule.setRoomId(roomId);
        existingSchedule.setAvailableDate(availableDate);
        existingSchedule.setTimePeriod(timePeriod);
        existingSchedule.setStartTime(startTime);
        existingSchedule.setEndTime(endTime);
        existingSchedule.setIsAvailable(isAvailable);
        
        return doctorScheduleRepository.save(existingSchedule);
    }
    
    /**
     * Update schedule availability
     * @param scheduleId Schedule ID
     * @param isAvailable Availability status
     * @return Updated schedule
     */
    public DoctorSchedule updateScheduleAvailability(Long scheduleId, Boolean isAvailable) {
        Optional<DoctorSchedule> schedule = doctorScheduleRepository.findById(scheduleId);
        if (schedule.isEmpty()) {
            throw new IllegalArgumentException("Schedule with ID " + scheduleId + " not found");
        }
        
        DoctorSchedule existingSchedule = schedule.get();
        existingSchedule.setIsAvailable(isAvailable);
        return doctorScheduleRepository.save(existingSchedule);
    }
    
    /**
     * Update schedule active status
     * @param scheduleId Schedule ID
     * @param isActive Active status
     * @return Updated schedule
     */
    public DoctorSchedule updateScheduleActiveStatus(Long scheduleId, Boolean isActive) {
        Optional<DoctorSchedule> schedule = doctorScheduleRepository.findById(scheduleId);
        if (schedule.isEmpty()) {
            throw new IllegalArgumentException("Schedule with ID " + scheduleId + " not found");
        }
        
        DoctorSchedule existingSchedule = schedule.get();
        existingSchedule.setIsActive(isActive);
        return doctorScheduleRepository.save(existingSchedule);
    }
    
    /**
     * Delete schedule
     * @param scheduleId Schedule ID
     * @return true if deleted successfully
     */
    public boolean deleteSchedule(Long scheduleId) {
        if (doctorScheduleRepository.existsById(scheduleId)) {
            doctorScheduleRepository.deleteById(scheduleId);
            return true;
        }
        return false;
    }
    
    /**
     * Get total schedule count
     * @return Total count of schedules
     */
    public long getTotalScheduleCount() {
        return doctorScheduleRepository.count();
    }
    
    /**
     * Get active schedule count
     * @return Count of active schedules
     */
    public long getActiveScheduleCount() {
        return doctorScheduleRepository.countByIsActiveTrue();
    }
    
    /**
     * Get available schedule count
     * @return Count of available schedules
     */
    public long getAvailableScheduleCount() {
        return doctorScheduleRepository.countByIsAvailableTrue();
    }
}
