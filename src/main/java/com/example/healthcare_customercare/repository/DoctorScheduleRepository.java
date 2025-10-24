package com.example.healthcare_customercare.repository;

import com.example.healthcare_customercare.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    
    /**
     * Find schedules by doctor ID
     * @param docId Doctor ID
     * @return List of schedules
     */
    List<DoctorSchedule> findByDocId(Long docId);
    
    /**
     * Find schedules by room ID
     * @param roomId Room ID
     * @return List of schedules
     */
    List<DoctorSchedule> findByRoomId(Long roomId);
    
    /**
     * Find schedules by doctor and room
     * @param docId Doctor ID
     * @param roomId Room ID
     * @return List of schedules
     */
    List<DoctorSchedule> findByDocIdAndRoomId(Long docId, Long roomId);
    
    /**
     * Find schedules by date
     * @param availableDate Available date
     * @return List of schedules
     */
    List<DoctorSchedule> findByAvailableDate(LocalDate availableDate);
    
    /**
     * Find schedules by doctor and date
     * @param docId Doctor ID
     * @param availableDate Available date
     * @return List of schedules
     */
    List<DoctorSchedule> findByDocIdAndAvailableDate(Long docId, LocalDate availableDate);
    
    /**
     * Find schedules by room and date
     * @param roomId Room ID
     * @param availableDate Available date
     * @return List of schedules
     */
    List<DoctorSchedule> findByRoomIdAndAvailableDate(Long roomId, LocalDate availableDate);
    
    /**
     * Find active schedules
     * @return List of active schedules
     */
    List<DoctorSchedule> findByIsActiveTrue();
    
    /**
     * Find available schedules
     * @return List of available schedules
     */
    List<DoctorSchedule> findByIsAvailableTrue();
    
    /**
     * Find active and available schedules
     * @return List of active and available schedules
     */
    List<DoctorSchedule> findByIsActiveTrueAndIsAvailableTrue();
    
    /**
     * Check if doctor is already scheduled for a specific time period
     * @param docId Doctor ID
     * @param availableDate Available date
     * @param timePeriod Time period
     * @return Optional<DoctorSchedule>
     */
    Optional<DoctorSchedule> findByDocIdAndAvailableDateAndTimePeriod(Long docId, LocalDate availableDate, String timePeriod);
    
    /**
     * Check if room is already assigned for a specific time period
     * @param roomId Room ID
     * @param availableDate Available date
     * @param timePeriod Time period
     * @return Optional<DoctorSchedule>
     */
    Optional<DoctorSchedule> findByRoomIdAndAvailableDateAndTimePeriod(Long roomId, LocalDate availableDate, String timePeriod);
    
    /**
     * Count schedules by doctor
     * @param docId Doctor ID
     * @return Count of schedules
     */
    long countByDocId(Long docId);
    
    /**
     * Count schedules by room
     * @param roomId Room ID
     * @return Count of schedules
     */
    long countByRoomId(Long roomId);
    
    /**
     * Count active schedules
     * @return Count of active schedules
     */
    long countByIsActiveTrue();
    
    /**
     * Count available schedules
     * @return Count of available schedules
     */
    long countByIsAvailableTrue();
}
