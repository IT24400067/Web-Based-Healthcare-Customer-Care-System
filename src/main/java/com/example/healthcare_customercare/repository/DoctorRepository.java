package com.example.healthcare_customercare.repository;

import com.example.healthcare_customercare.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    /**
     * Find doctor by doctor name
     * @param doctorName Doctor's name
     * @return Optional<Doctor>
     */
    Optional<Doctor> findByDoctorName(String doctorName);
    
    /**
     * Find doctor by email
     * @param email Doctor's email
     * @return Optional<Doctor>
     */
    Optional<Doctor> findByEmail(String email);
    
    /**
     * Search doctors by name containing text
     * @param name Text to search in doctor name
     * @return List of doctors
     */
    List<Doctor> findByDoctorNameContainingIgnoreCase(String name);
    
    /**
     * Find doctors by specialization
     * @param specialization Doctor's specialization
     * @return List of doctors
     */
    List<Doctor> findBySpecialization(String specialization);
    
    /**
     * Find doctors by department
     * @param department Doctor's department
     * @return List of doctors
     */
    List<Doctor> findByDepartment(String department);
    
    /**
     * Count doctors by specialization
     * @param specialization Doctor's specialization
     * @return Count of doctors
     */
    long countBySpecialization(String specialization);
    
    /**
     * Count doctors by department
     * @param department Doctor's department
     * @return Count of doctors
     */
    long countByDepartment(String department);
}
