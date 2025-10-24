package com.example.healthcare_customercare.service;

import com.example.healthcare_customercare.entity.Doctor;
import com.example.healthcare_customercare.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private DoctorScheduleService doctorScheduleService;
    
    /**
     * Get all doctors
     * @return List of all doctors
     */
    public List<Doctor> getAllDoctors() {
        try {
            return doctorRepository.findAll();
        } catch (Exception e) {
            // If database fails, return empty list
            System.err.println("Database error in getAllDoctors: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
    
    /**
     * Get doctor by ID
     * @param docId Doctor's ID
     * @return Optional<Doctor>
     */
    public Optional<Doctor> getDoctorById(Long docId) {
        return doctorRepository.findById(docId);
    }
    
    /**
     * Get doctor by doctor name
     * @param doctorName Doctor's name
     * @return Optional<Doctor>
     */
    public Optional<Doctor> getDoctorByName(String doctorName) {
        return doctorRepository.findByDoctorName(doctorName);
    }
    
    /**
     * Get doctor by email
     * @param email Doctor's email
     * @return Optional<Doctor>
     */
    public Optional<Doctor> getDoctorByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }
    
    /**
     * Get doctors by specialization
     * @param specialization Doctor's specialization
     * @return List of doctors
     */
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization);
    }
    
    /**
     * Get doctors by department
     * @param department Doctor's department
     * @return List of doctors
     */
    public List<Doctor> getDoctorsByDepartment(String department) {
        return doctorRepository.findByDepartment(department);
    }
    
    /**
     * Search doctors by name
     * @param name Text to search in doctor name
     * @return List of doctors
     */
    public List<Doctor> searchDoctorsByName(String name) {
        return doctorRepository.findByDoctorNameContainingIgnoreCase(name);
    }
    
    /**
     * Add a new doctor
     * @param doctorName Doctor's name
     * @param specialization Doctor's specialization
     * @param department Doctor's department
     * @param email Doctor's email
     * @return Created doctor
     */
    public Doctor addDoctor(String doctorName, String specialization, String department, String email) {
        try {
            // Check if doctor with same name already exists
            if (doctorRepository.findByDoctorName(doctorName).isPresent()) {
                throw new IllegalArgumentException("Doctor with name '" + doctorName + "' already exists");
            }
            
            // Check if doctor with same email already exists (if email is provided)
            if (email != null && !email.trim().isEmpty() && doctorRepository.findByEmail(email).isPresent()) {
                throw new IllegalArgumentException("Doctor with email '" + email + "' already exists");
            }
            
            Doctor doctor = new Doctor(doctorName, specialization, department, email);
            return doctorRepository.save(doctor);
        } catch (Exception e) {
            System.err.println("Database error in addDoctor: " + e.getMessage());
            throw new RuntimeException("Failed to add doctor: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update doctor information
     * @param docId Doctor's ID
     * @param doctorName Doctor's name
     * @param specialization Doctor's specialization
     * @param department Doctor's department
     * @param email Doctor's email
     * @return Updated doctor
     */
    public Doctor updateDoctor(Long docId, String doctorName, String specialization, String department, String email) {
        Optional<Doctor> existingDoctor = doctorRepository.findById(docId);
        if (existingDoctor.isEmpty()) {
            throw new IllegalArgumentException("Doctor with ID " + docId + " not found");
        }
        
        Doctor doctor = existingDoctor.get();
        doctor.setDoctorName(doctorName);
        doctor.setSpecialization(specialization);
        doctor.setDepartment(department);
        doctor.setEmail(email);
        
        return doctorRepository.save(doctor);
    }
    
    /**
     * Delete doctor
     * @param docId Doctor's ID
     * @return true if deleted successfully
     */
    public boolean deleteDoctor(Long docId) {
        try {
            if (doctorRepository.existsById(docId)) {
                // First delete all associated schedules
                List<com.example.healthcare_customercare.entity.DoctorSchedule> schedules = doctorScheduleService.getSchedulesByDoctor(docId);
                for (com.example.healthcare_customercare.entity.DoctorSchedule schedule : schedules) {
                    doctorScheduleService.deleteSchedule(schedule.getDoctorScheduleId());
                }
                
                // Then delete the doctor
                doctorRepository.deleteById(docId);
                return true;
            }
            return false;
        } catch (Exception e) {
            // Handle any errors
            System.err.println("Error deleting doctor: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if doctor exists by name
     * @param doctorName Doctor's name
     * @return true if doctor exists
     */
    public boolean doctorExistsByName(String doctorName) {
        return doctorRepository.findByDoctorName(doctorName).isPresent();
    }
    
    /**
     * Check if doctor exists by email
     * @param email Doctor's email
     * @return true if doctor exists
     */
    public boolean doctorExistsByEmail(String email) {
        return doctorRepository.findByEmail(email).isPresent();
    }
    
    /**
     * Check if doctor exists by ID
     * @param docId Doctor's ID
     * @return true if doctor exists
     */
    public boolean existsById(Long docId) {
        return doctorRepository.existsById(docId);
    }
    
    
    /**
     * Get doctor count by specialization
     * @param specialization Doctor's specialization
     * @return Count of doctors
     */
    public long getDoctorCountBySpecialization(String specialization) {
        return doctorRepository.countBySpecialization(specialization);
    }
    
    /**
     * Get doctor count by department
     * @param department Doctor's department
     * @return Count of doctors
     */
    public long getDoctorCountByDepartment(String department) {
        return doctorRepository.countByDepartment(department);
    }
    
    /**
     * Get total doctor count
     * @return Total count of doctors
     */
    public long getTotalDoctorCount() {
        return doctorRepository.count();
    }
}
