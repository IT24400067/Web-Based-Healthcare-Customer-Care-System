package com.example.healthcare_customercare.controller;

import com.example.healthcare_customercare.entity.DoctorSchedule;
import com.example.healthcare_customercare.entity.Doctor;
import com.example.healthcare_customercare.service.DoctorScheduleService;
import com.example.healthcare_customercare.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleApiController {

    @Autowired
    private DoctorScheduleService doctorScheduleService;
    
    @Autowired
    private DoctorService doctorService;

    /**
     * Get all schedules for a specific doctor
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorSchedule>> getDoctorSchedules(@PathVariable Long doctorId) {
        try {
            List<DoctorSchedule> schedules = doctorScheduleService.getSchedulesByDoctor(doctorId);
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get available schedules for a specific doctor and date
     */
    @GetMapping("/doctor/{doctorId}/date/{date}")
    public ResponseEntity<List<DoctorSchedule>> getDoctorSchedulesByDate(
            @PathVariable Long doctorId, 
            @PathVariable String date) {
        try {
            LocalDate availableDate = LocalDate.parse(date);
            List<DoctorSchedule> schedules = doctorScheduleService.getSchedulesByDoctor(doctorId)
                .stream()
                .filter(schedule -> schedule.getAvailableDate().equals(availableDate))
                .filter(schedule -> schedule.getIsActive() && schedule.getIsAvailable())
                .toList();
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all available schedules for appointment booking
     */
    @GetMapping("/available")
    public ResponseEntity<List<DoctorSchedule>> getAvailableSchedules() {
        try {
            List<DoctorSchedule> schedules = doctorScheduleService.getAvailableSchedules()
                .stream()
                .filter(schedule -> schedule.getIsActive())
                .toList();
            return ResponseEntity.ok(schedules);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all doctors for appointment booking
     */
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getDoctors() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            return ResponseEntity.ok(doctors);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get a specific schedule by ID
     */
    @GetMapping("/{scheduleId}")
    public ResponseEntity<DoctorSchedule> getSchedule(@PathVariable Long scheduleId) {
        try {
            Optional<DoctorSchedule> schedule = doctorScheduleService.getScheduleById(scheduleId);
            if (schedule.isPresent()) {
                return ResponseEntity.ok(schedule.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update a schedule
     */
    @PutMapping("/{scheduleId}")
    public ResponseEntity<DoctorSchedule> updateSchedule(
            @PathVariable Long scheduleId,
            @RequestBody ScheduleUpdateRequest request) {
        try {
            // Parse the date
            LocalDate availableDate = LocalDate.parse(request.getAvailableDate());
            
            // Get the existing schedule to preserve doctor ID
            Optional<DoctorSchedule> existingSchedule = doctorScheduleService.getScheduleById(scheduleId);
            if (existingSchedule.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            // Update the schedule with new values
            DoctorSchedule updatedSchedule = doctorScheduleService.updateScheduleDetails(
                scheduleId,
                request.getRoomId(),
                availableDate,
                request.getTimePeriod(),
                request.getIsAvailable()
            );
            
            return ResponseEntity.ok(updatedSchedule);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Delete a schedule
     */
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long scheduleId) {
        try {
            boolean deleted = doctorScheduleService.deleteSchedule(scheduleId);
            if (deleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Request class for schedule updates
     */
    public static class ScheduleUpdateRequest {
        private Long roomId;
        private String timePeriod;
        private String availableDate;
        private Boolean isAvailable;

        // Getters and setters
        public Long getRoomId() {
            return roomId;
        }

        public void setRoomId(Long roomId) {
            this.roomId = roomId;
        }

        public String getTimePeriod() {
            return timePeriod;
        }

        public void setTimePeriod(String timePeriod) {
            this.timePeriod = timePeriod;
        }

        public String getAvailableDate() {
            return availableDate;
        }

        public void setAvailableDate(String availableDate) {
            this.availableDate = availableDate;
        }

        public Boolean getIsAvailable() {
            return isAvailable;
        }

        public void setIsAvailable(Boolean isAvailable) {
            this.isAvailable = isAvailable;
        }
    }
}
