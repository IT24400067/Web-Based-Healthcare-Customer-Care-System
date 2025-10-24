package com.example.healthcare_customercare.repository;

import com.example.healthcare_customercare.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    /**
     * Find appointments by patient email
     * @param patientEmail Patient's email
     * @return List of appointments
     */
    List<Appointment> findByPatientEmail(String patientEmail);
    
    /**
     * Find appointments by doctor ID
     * @param docId Doctor ID
     * @return List of appointments
     */
    List<Appointment> findByDocId(Long docId);
    
    /**
     * Find appointments by room ID
     * @param roomId Room ID
     * @return List of appointments
     */
    List<Appointment> findByRoomId(Long roomId);
    
    /**
     * Find appointments by appointment date
     * @param appointmentDate Appointment date
     * @return List of appointments
     */
    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);
    
    /**
     * Find appointments by doctor and date
     * @param docId Doctor ID
     * @param appointmentDate Appointment date
     * @return List of appointments
     */
    List<Appointment> findByDocIdAndAppointmentDate(Long docId, LocalDate appointmentDate);
    
    /**
     * Find appointments by room and date
     * @param roomId Room ID
     * @param appointmentDate Appointment date
     * @return List of appointments
     */
    List<Appointment> findByRoomIdAndAppointmentDate(Long roomId, LocalDate appointmentDate);
    
    /**
     * Find appointments by schedule ID
     * @param scheduleId Schedule ID
     * @return Optional<Appointment>
     */
    Optional<Appointment> findByScheduleId(Long scheduleId);
    
    /**
     * Check if appointment exists for a specific schedule
     * @param scheduleId Schedule ID
     * @return true if appointment exists
     */
    boolean existsByScheduleId(Long scheduleId);
    
    /**
     * Count appointments by doctor
     * @param docId Doctor ID
     * @return Count of appointments
     */
    long countByDocId(Long docId);
    
    /**
     * Count appointments by room
     * @param roomId Room ID
     * @return Count of appointments
     */
    long countByRoomId(Long roomId);
    
    /**
     * Count appointments by date
     * @param appointmentDate Appointment date
     * @return Count of appointments
     */
    long countByAppointmentDate(LocalDate appointmentDate);
    
    /**
     * Custom query to find upcoming appointments
     * @param currentDate Current date
     * @return List of upcoming appointments
     */
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate >= :currentDate ORDER BY a.appointmentDate ASC")
    List<Appointment> findUpcomingAppointments(@Param("currentDate") LocalDate currentDate);
    
    /**
     * Custom query to find appointments for a specific date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of appointments
     */
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate BETWEEN :startDate AND :endDate ORDER BY a.appointmentDate ASC")
    List<Appointment> findAppointmentsByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
