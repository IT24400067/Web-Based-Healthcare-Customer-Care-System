package com.example.healthcare_customercare.service;

import com.example.healthcare_customercare.entity.Appointment;
import com.example.healthcare_customercare.entity.DoctorSchedule;
import com.example.healthcare_customercare.repository.AppointmentRepository;
import com.example.healthcare_customercare.repository.DoctorScheduleRepository;
import com.example.healthcare_customercare.service.RoomService;
import com.example.healthcare_customercare.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private NotificationService notificationService;
    
    /**
     * Create a new appointment (for receptionist use)
     * @param patientName Patient's name
     * @param patientEmail Patient's email
     * @param appointmentDate Appointment date
     * @param docId Doctor ID
     * @param roomId Room ID
     * @param scheduleId Schedule ID
     * @return Created appointment
     */
    @Transactional
    public Appointment createAppointment(String patientName, String patientEmail, LocalDate appointmentDate,
                                       Long docId, Long roomId, Long scheduleId) {
        System.out.println("Creating appointment with parameters:");
        System.out.println("  Patient Name: " + patientName);
        System.out.println("  Patient Email: " + patientEmail);
        System.out.println("  Appointment Date: " + appointmentDate);
        System.out.println("  Doctor ID: " + docId);
        System.out.println("  Room ID: " + roomId);
        System.out.println("  Schedule ID: " + scheduleId);
        
        // Check if appointment already exists for this schedule
        if (appointmentRepository.existsByScheduleId(scheduleId)) {
            System.out.println("ERROR: Appointment already exists for schedule ID: " + scheduleId);
            throw new IllegalArgumentException("Appointment already exists for this schedule");
        }
        
        // Check if the schedule is available
        Optional<DoctorSchedule> scheduleOpt = doctorScheduleRepository.findById(scheduleId);
        if (scheduleOpt.isEmpty()) {
            System.out.println("ERROR: Schedule not found with ID: " + scheduleId);
            throw new IllegalArgumentException("Schedule not found with ID: " + scheduleId);
        }
        
        DoctorSchedule schedule = scheduleOpt.get();
        if (!schedule.getIsAvailable()) {
            System.out.println("ERROR: Schedule is not available. Schedule ID: " + scheduleId);
            throw new IllegalArgumentException("Schedule is not available");
        }
        
        // Create the appointment
        Appointment appointment = new Appointment(patientName, patientEmail, appointmentDate, docId, roomId, scheduleId);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        // Mark the schedule as unavailable
        schedule.setIsAvailable(false);
        doctorScheduleRepository.save(schedule);
        
        // Increment room capacity
        try {
            roomService.incrementRoomCapacity(roomId);
            System.out.println("Successfully incremented room capacity for room ID: " + roomId);
        } catch (Exception e) {
            System.err.println("Error updating room capacity: " + e.getMessage());
            // Don't fail the appointment creation if room capacity update fails
        }
        
        System.out.println("Successfully created appointment with ID: " + savedAppointment.getId());
        
        // Send notification to patient
        try {
            String message = "Your appointment has been scheduled for " + appointmentDate + ". Please arrive 15 minutes early.";
            notificationService.createAppointmentNotification(patientEmail, message, savedAppointment.getId());
            System.out.println("Appointment notification sent to: " + patientEmail);
        } catch (Exception e) {
            System.err.println("Error sending appointment notification: " + e.getMessage());
        }
        
        return savedAppointment;
    }
    
    /**
     * Book a new appointment
     * @param patientName Patient's name
     * @param patientEmail Patient's email
     * @param appointmentDate Appointment date
     * @param timeSlot Time slot
     * @param docId Doctor ID
     * @param roomId Room ID
     * @param scheduleId Schedule ID
     * @return Created appointment
     */
    @Transactional
    public Appointment bookAppointment(String patientName, String patientEmail, LocalDate appointmentDate,
                                     String timeSlot, Long docId, Long roomId, Long scheduleId) {
        System.out.println("Booking appointment with parameters:");
        System.out.println("  Patient Name: " + patientName);
        System.out.println("  Patient Email: " + patientEmail);
        System.out.println("  Appointment Date: " + appointmentDate);
        System.out.println("  Time Slot: " + timeSlot);
        System.out.println("  Doctor ID: " + docId);
        System.out.println("  Room ID: " + roomId);
        System.out.println("  Schedule ID: " + scheduleId);
        
        // Check if appointment already exists for this schedule
        if (appointmentRepository.existsByScheduleId(scheduleId)) {
            System.out.println("ERROR: Appointment already exists for schedule ID: " + scheduleId);
            throw new IllegalArgumentException("Appointment already exists for this schedule");
        }
        
        // Verify the schedule exists and is available
        Optional<DoctorSchedule> scheduleOpt = doctorScheduleRepository.findById(scheduleId);
        if (scheduleOpt.isEmpty()) {
            System.out.println("ERROR: Schedule not found with ID: " + scheduleId);
            throw new IllegalArgumentException("Schedule not found");
        }
        
        DoctorSchedule schedule = scheduleOpt.get();
        System.out.println("Found schedule: " + schedule);
        
        // Verify the schedule matches the appointment details
        if (!schedule.getDocId().equals(docId)) {
            System.out.println("ERROR: Schedule doctor ID (" + schedule.getDocId() + ") doesn't match requested doctor ID (" + docId + ")");
            throw new IllegalArgumentException("Schedule doctor ID doesn't match");
        }
        
        if (!schedule.getAvailableDate().equals(appointmentDate)) {
            System.out.println("ERROR: Schedule date (" + schedule.getAvailableDate() + ") doesn't match requested date (" + appointmentDate + ")");
            throw new IllegalArgumentException("Schedule date doesn't match");
        }
        
        if (!schedule.getRoomId().equals(roomId)) {
            System.out.println("ERROR: Schedule room ID (" + schedule.getRoomId() + ") doesn't match requested room ID (" + roomId + ")");
            throw new IllegalArgumentException("Schedule room ID doesn't match");
        }
        
        if (!schedule.getIsActive()) {
            System.out.println("ERROR: Schedule is not active");
            throw new IllegalArgumentException("Schedule is not active");
        }
        
        if (!schedule.getIsAvailable()) {
            System.out.println("ERROR: Schedule is not available");
            throw new IllegalArgumentException("Schedule is not available");
        }
        
        // Create the appointment
        Appointment appointment = new Appointment(patientName, patientEmail, appointmentDate, 
                                                 docId, roomId, scheduleId);
        
        System.out.println("Creating appointment: " + appointment);
        
        // Save the appointment
        Appointment savedAppointment = appointmentRepository.save(appointment);
        System.out.println("Saved appointment with ID: " + savedAppointment.getId());
        
        // Mark the schedule as unavailable
        schedule.setIsAvailable(false);
        doctorScheduleRepository.save(schedule);
        System.out.println("Marked schedule as unavailable");
        
        // Increment room capacity
        try {
            roomService.incrementRoomCapacity(roomId);
            System.out.println("Successfully incremented room capacity for room ID: " + roomId);
        } catch (Exception e) {
            System.err.println("Error updating room capacity: " + e.getMessage());
            // Don't fail the appointment creation if room capacity update fails
        }
        
        // Send notification to patient
        try {
            String message = "Your appointment has been booked for " + appointmentDate + " at " + timeSlot + ". Please arrive 15 minutes early.";
            notificationService.createAppointmentNotification(patientEmail, message, savedAppointment.getId());
            System.out.println("Appointment booking notification sent to: " + patientEmail);
        } catch (Exception e) {
            System.err.println("Error sending appointment booking notification: " + e.getMessage());
        }
        
        return savedAppointment;
    }
    
    /**
     * Get appointment by ID
     * @param appointmentId Appointment ID
     * @return Optional<Appointment>
     */
    public Optional<Appointment> getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }
    
    /**
     * Get all appointments
     * @return List of all appointments
     */
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    
    /**
     * Get appointments by patient email
     * @param patientEmail Patient's email
     * @return List of appointments
     */
    public List<Appointment> getAppointmentsByPatient(String patientEmail) {
        return appointmentRepository.findByPatientEmail(patientEmail);
    }
    
    /**
     * Get appointments by doctor ID
     * @param docId Doctor ID
     * @return List of appointments
     */
    public List<Appointment> getAppointmentsByDoctor(Long docId) {
        return appointmentRepository.findByDocId(docId);
    }
    
    /**
     * Get appointments by room ID
     * @param roomId Room ID
     * @return List of appointments
     */
    public List<Appointment> getAppointmentsByRoom(Long roomId) {
        return appointmentRepository.findByRoomId(roomId);
    }
    
    /**
     * Get appointments by date
     * @param appointmentDate Appointment date
     * @return List of appointments
     */
    public List<Appointment> getAppointmentsByDate(LocalDate appointmentDate) {
        return appointmentRepository.findByAppointmentDate(appointmentDate);
    }
    
    /**
     * Get upcoming appointments
     * @return List of upcoming appointments
     */
    public List<Appointment> getUpcomingAppointments() {
        return appointmentRepository.findUpcomingAppointments(LocalDate.now());
    }
    
    /**
     * Get appointments by date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of appointments
     */
    public List<Appointment> getAppointmentsByDateRange(LocalDate startDate, LocalDate endDate) {
        return appointmentRepository.findAppointmentsByDateRange(startDate, endDate);
    }
    
    /**
     * Get total appointment count
     * @return Total count of appointments
     */
    public long getTotalAppointmentCount() {
        return appointmentRepository.count();
    }
    
    /**
     * Get appointment count by doctor
     * @param docId Doctor ID
     * @return Count of appointments
     */
    public long getAppointmentCountByDoctor(Long docId) {
        return appointmentRepository.countByDocId(docId);
    }
    
    /**
     * Get appointment count by room
     * @param roomId Room ID
     * @return Count of appointments
     */
    public long getAppointmentCountByRoom(Long roomId) {
        return appointmentRepository.countByRoomId(roomId);
    }
    
    /**
     * Update an existing appointment
     * @param appointmentId Appointment ID to update
     * @param patientName Updated patient name
     * @param patientEmail Updated patient email
     * @param appointmentDate Updated appointment date
     * @param docId Updated doctor ID
     * @param roomId Updated room ID
     * @param scheduleId Updated schedule ID
     * @return Updated appointment
     * @throws IllegalArgumentException if appointment not found
     */
    @Transactional
    public Appointment updateAppointment(Long appointmentId, String patientName, String patientEmail,
                                       LocalDate appointmentDate, Long docId, Long roomId, Long scheduleId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new IllegalArgumentException("Appointment not found with ID: " + appointmentId);
        }
        
        Appointment appointment = appointmentOpt.get();
        
        // If schedule ID is changing, we need to handle the old and new schedules
        if (!appointment.getScheduleId().equals(scheduleId)) {
            // Free up the old schedule
            Optional<DoctorSchedule> oldScheduleOpt = doctorScheduleRepository.findById(appointment.getScheduleId());
            if (oldScheduleOpt.isPresent()) {
                DoctorSchedule oldSchedule = oldScheduleOpt.get();
                oldSchedule.setIsAvailable(true);
                doctorScheduleRepository.save(oldSchedule);
            }
            
            // Reserve the new schedule
            Optional<DoctorSchedule> newScheduleOpt = doctorScheduleRepository.findById(scheduleId);
            if (newScheduleOpt.isEmpty()) {
                throw new IllegalArgumentException("New schedule not found with ID: " + scheduleId);
            }
            
            DoctorSchedule newSchedule = newScheduleOpt.get();
            if (!newSchedule.getIsAvailable()) {
                throw new IllegalArgumentException("New schedule is not available");
            }
            
            newSchedule.setIsAvailable(false);
            doctorScheduleRepository.save(newSchedule);
        }
        
        // Update appointment details
        appointment.setPatientName(patientName);
        appointment.setPatientEmail(patientEmail);
        appointment.setAppointmentDate(appointmentDate);
        appointment.setDocId(docId);
        appointment.setRoomId(roomId);
        appointment.setScheduleId(scheduleId);
        
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        
        // Send notification to patient about appointment update
        try {
            String message = "Your appointment has been updated. New date: " + appointmentDate + ". Please check your appointment details.";
            notificationService.createAppointmentNotification(patientEmail, message, updatedAppointment.getId());
            System.out.println("Appointment update notification sent to: " + patientEmail);
        } catch (Exception e) {
            System.err.println("Error sending appointment update notification: " + e.getMessage());
        }
        
        return updatedAppointment;
    }
    
    /**
     * Delete an appointment
     * @param appointmentId Appointment ID to delete
     * @return true if deleted successfully, false if not found
     */
    @Transactional
    public boolean deleteAppointment(Long appointmentId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isEmpty()) {
            return false;
        }
        
        Appointment appointment = appointmentOpt.get();
        
        // Free up the schedule
        Optional<DoctorSchedule> scheduleOpt = doctorScheduleRepository.findById(appointment.getScheduleId());
        if (scheduleOpt.isPresent()) {
            DoctorSchedule schedule = scheduleOpt.get();
            schedule.setIsAvailable(true);
            doctorScheduleRepository.save(schedule);
        }
        
        // Decrement room capacity
        try {
            roomService.decrementRoomCapacity(appointment.getRoomId());
            System.out.println("Successfully decremented room capacity for room ID: " + appointment.getRoomId());
        } catch (Exception e) {
            System.err.println("Error updating room capacity: " + e.getMessage());
            // Don't fail the appointment deletion if room capacity update fails
        }
        
        // Send notification to patient about appointment cancellation
        try {
            String message = "Your appointment scheduled for " + appointment.getAppointmentDate() + " has been cancelled. Please contact us to reschedule if needed.";
            notificationService.createAppointmentNotification(appointment.getPatientEmail(), message, appointmentId);
            System.out.println("Appointment cancellation notification sent to: " + appointment.getPatientEmail());
        } catch (Exception e) {
            System.err.println("Error sending appointment cancellation notification: " + e.getMessage());
        }
        
        // Delete the appointment
        appointmentRepository.deleteById(appointmentId);
        return true;
    }
}
