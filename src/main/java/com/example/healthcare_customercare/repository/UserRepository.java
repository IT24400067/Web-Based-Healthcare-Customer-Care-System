package com.example.healthcare_customercare.repository;

import com.example.healthcare_customercare.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    // Find user by email
    Optional<User> findByEmail(String email);
    
    // Find user by email and role
    Optional<User> findByEmailAndRole(String email, String role);
    
    // Find all users by role
    List<User> findByRole(String role);
    
    // Check if email exists
    boolean existsByEmail(String email);
    
    // Check if email exists for a specific role
    boolean existsByEmailAndRole(String email, String role);
    
    // Custom query to authenticate user
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
    Optional<User> authenticateUser(@Param("email") String email, @Param("password") String password);
    
    // Custom query to authenticate customer
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password AND u.role = 'CUSTOMER'")
    Optional<User> authenticateCustomer(@Param("email") String email, @Param("password") String password);
    
    // Count users by role
    long countByRole(String role);
    
    // Count users by role not equal to specified role
    long countByRoleNot(String role);
    
    // Search users by first name, last name, or email (case insensitive)
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        String firstName, String lastName, String email);
}