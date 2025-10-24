package com.example.healthcare_customercare.service;

import com.example.healthcare_customercare.entity.Role;
import com.example.healthcare_customercare.entity.User;
import com.example.healthcare_customercare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SystemAdminService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleService roleService;

    // Find user by email
    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findById(email);
        return user.orElse(null);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Create a new user
    public User createUser(User user) {
        // Validate user data
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }

        // Check if user already exists
        if (userRepository.existsById(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }

        // Validate role
        if (!isValidRole(user.getRole())) {
            throw new IllegalArgumentException("Invalid role: " + user.getRole());
        }

        // Store password as plain text
        // Password is already in plain text, no need to hash

        return userRepository.save(user);
    }

    // Update an existing user
    public User updateUser(User user) {
        // Validate user data
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }

        // Check if user exists
        if (!userRepository.existsById(user.getEmail())) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " does not exist");
        }

        // Validate role
        if (!isValidRole(user.getRole())) {
            throw new IllegalArgumentException("Invalid role: " + user.getRole());
        }

        // If password is provided and not empty, store as plain text
        // Password is already in plain text, no need to hash

        return userRepository.save(user);
    }

    // Delete a user
    public boolean deleteUser(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        // Check if user exists
        if (!userRepository.existsById(email)) {
            throw new IllegalArgumentException("User with email " + email + " does not exist");
        }

        // Get user to check if it's an admin
        Optional<User> user = userRepository.findById(email);
        if (user.isPresent() && user.get().isAdmin()) {
            throw new IllegalArgumentException("Cannot delete admin users");
        }

        try {
            userRepository.deleteById(email);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user: " + e.getMessage());
        }
    }

    // Get users by role
    public List<User> getUsersByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }

        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        return userRepository.findByRole(role);
    }

    // Search users by name or email
    public List<User> searchUsers(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllUsers();
        }

        String searchQuery = "%" + query.toLowerCase() + "%";
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            searchQuery, searchQuery, searchQuery);
    }

    // Get user statistics
    public Map<String, Long> getUserStatistics() {
        Map<String, Long> stats = new HashMap<>();
        
        // Total users
        long totalUsers = userRepository.count();
        stats.put("totalUsers", totalUsers);
        
        // Active users (for now, we'll consider all users as active)
        // In a real application, you might track last login time
        stats.put("activeUsers", totalUsers);
        
        // Staff members (all non-customer roles)
        long staffMembers = userRepository.countByRoleNot("CUSTOMER");
        stats.put("staffMembers", staffMembers);
        
        // Customers
        long customers = userRepository.countByRole("CUSTOMER");
        stats.put("customers", customers);
        
        return stats;
    }

    // Get user count by role
    public long getUserCountByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role is required");
        }
        return userRepository.countByRole(role);
    }

    // Check if role is valid
    private boolean isValidRole(String role) {
        if (role == null) {
            return false;
        }
        
        // Check if the role exists in the database
        return roleService.roleNameExists(role);
    }

    // Get all available roles
    public List<String> getAvailableRoles() {
        return roleService.getAllRoles().stream()
                .map(Role::getRoleName)
                .collect(java.util.stream.Collectors.toList());
    }

    // Get role display name
    public String getRoleDisplayName(String role) {
        if (role == null) {
            return "Unknown";
        }
        
        switch (role.toUpperCase()) {
            case "CUSTOMER":
                return "Customer";
            case "RECEPTIONIST":
                return "Receptionist";
            case "STAFF_COORDINATOR":
                return "Staff Coordinator";
            case "CUSTOMER_SUPPORT_MANAGER":
                return "Customer Support Manager";
            case "SENIOR_MEDICAL_OFFICER":
                return "Senior Medical Officer";
            case "ADMIN":
                return "System Admin";
            default:
                return role;
        }
    }

    // Get role description
    public String getRoleDescription(String role) {
        if (role == null) {
            return "Unknown role";
        }
        
        switch (role.toUpperCase()) {
            case "CUSTOMER":
                return "Patients who can book appointments and access their medical records";
            case "RECEPTIONIST":
                return "Front desk staff who manage appointments and patient registration";
            case "STAFF_COORDINATOR":
                return "Manages doctor schedules and staff assignments";
            case "CUSTOMER_SUPPORT_MANAGER":
                return "Handles customer support tickets and feedback";
            case "SENIOR_MEDICAL_OFFICER":
                return "Senior medical staff with access to patient history and reports";
            case "ADMIN":
                return "Full system access for user management and system administration";
            default:
                return "Unknown role";
        }
    }

    // Check if user can be deleted (not admin)
    public boolean canDeleteUser(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        Optional<User> user = userRepository.findById(email);
        if (user.isPresent()) {
            return !user.get().isAdmin();
        }
        
        return false;
    }

    // Get user permissions by role
    public List<String> getUserPermissions(String role) {
        if (role == null) {
            return List.of();
        }
        
        switch (role.toUpperCase()) {
            case "CUSTOMER":
                return List.of(
                    "Book appointments",
                    "View medical reports", 
                    "Submit feedback",
                    "Create support tickets"
                );
            case "RECEPTIONIST":
                return List.of(
                    "Manage appointments",
                    "Register patients",
                    "View room availability",
                    "Update patient info"
                );
            case "STAFF_COORDINATOR":
                return List.of(
                    "Manage doctor schedules",
                    "Assign staff",
                    "View system reports",
                    "Manage rooms"
                );
            case "CUSTOMER_SUPPORT_MANAGER":
                return List.of(
                    "Manage support tickets",
                    "Respond to feedback",
                    "View customer issues",
                    "Generate reports"
                );
            case "SENIOR_MEDICAL_OFFICER":
                return List.of(
                    "View patient history",
                    "Access medical reports",
                    "Manage recommendations",
                    "Review cases"
                );
            case "ADMIN":
                return List.of(
                    "Manage all users",
                    "Assign roles",
                    "System configuration",
                    "Full access"
                );
            default:
                return List.of();
        }
    }
}
