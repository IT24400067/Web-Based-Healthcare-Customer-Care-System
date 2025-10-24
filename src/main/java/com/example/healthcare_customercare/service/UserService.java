package com.example.healthcare_customercare.service;

import com.example.healthcare_customercare.entity.User;
import com.example.healthcare_customercare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Return password as plain text (no hashing)
    public String hashPassword(String password) {
        return password;
    }
    
    private boolean verifyPassword(String password, String storedPassword) {
        System.out.println("=== PASSWORD VERIFICATION DEBUG ===");
        System.out.println("Input password length: " + (password != null ? password.length() : "null"));
        System.out.println("Stored password length: " + (storedPassword != null ? storedPassword.length() : "null"));
        
        // Simple plain text comparison
        boolean result = password.equals(storedPassword);
        System.out.println("Plain text verification result: " + result);
        return result;
    }
    
    
    /**
     * Authenticate user with email and password
     * @param email User's email
     * @param password User's password
     * @return Optional<User> if authentication successful
     */
    public Optional<User> authenticateUser(String email, String password) {
        System.out.println("=== USER SERVICE AUTH DEBUG ===");
        System.out.println("Looking for user with email: " + email);
        
        Optional<User> user = userRepository.findByEmail(email);
        System.out.println("User found in DB: " + user.isPresent());
        
        if (user.isPresent()) {
            User foundUser = user.get();
            System.out.println("Found user role: " + foundUser.getRole());
            System.out.println("Stored password (first 10 chars): " + 
                (foundUser.getPassword() != null ? foundUser.getPassword().substring(0, Math.min(10, foundUser.getPassword().length())) : "null"));
            System.out.println("Input password (first 10 chars): " + 
                (password != null ? password.substring(0, Math.min(10, password.length())) : "null"));
            
            boolean passwordMatch = verifyPassword(password, foundUser.getPassword());
            System.out.println("Password verification result: " + passwordMatch);
            
            if (passwordMatch) {
                return user;
            }
        }
        return Optional.empty();
    }
    
    /**
     * Authenticate customer specifically
     * @param email Customer's email
     * @param password Customer's password
     * @return Optional<User> if customer authentication successful
     */
    public Optional<User> authenticateCustomer(String email, String password) {
        Optional<User> customer = userRepository.findByEmailAndRole(email, "CUSTOMER");
        if (customer.isPresent() && verifyPassword(password, customer.get().getPassword())) {
            return customer;
        }
        return Optional.empty();
    }
    
    /**
     * Find user by email
     * @param email User's email
     * @return Optional<User>
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Find user by email (alias for findByEmail)
     * @param email User's email
     * @return Optional<User>
     */
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    /**
     * Find customer by email
     * @param email Customer's email
     * @return Optional<User>
     */
    public Optional<User> findCustomerByEmail(String email) {
        return userRepository.findByEmailAndRole(email, "CUSTOMER");
    }
    
    /**
     * Save user
     * @param user User to save
     * @return Saved user
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    /**
     * Find all users by role
     * @param role User role
     * @return List of users with the specified role
     */
    
    /**
     * Find all users by role
     * @param role User role
     * @return List of users with the specified role
     */
    public List<User> findUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
    
    /**
     * Check if email exists
     * @param email Email to check
     * @return true if email exists
     */
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
    
    /**
     * Check if customer email exists
     * @param email Customer email to check
     * @return true if customer email exists
     */
    public boolean customerEmailExists(String email) {
        return userRepository.existsByEmailAndRole(email, "CUSTOMER");
    }
    
    /**
     * Get all customers
     * @return List of customers
     */
    public List<User> getAllCustomers() {
        return userRepository.findByRole("CUSTOMER");
    }
    
    /**
     * Find users by role
     * @param role Role to search for
     * @return List of users with the specified role
     */
    public List<User> findByRole(String role) {
        return userRepository.findByRole(role);
    }
    
    /**
     * Create a new customer
     * @param firstName Customer's first name
     * @param lastName Customer's last name
     * @param email Customer's email
     * @param phoneNumber Customer's phone number
     * @param password Customer's password
     * @return Created customer
     */
    public User createCustomer(String firstName, String lastName, String email, String phoneNumber, String password) {
        // Store password as plain text
        User customer = new User(firstName, lastName, email, phoneNumber, password, "CUSTOMER");
        return userRepository.save(customer);
    }
    
    /**
     * Create a new staff coordinator
     * @param firstName Staff coordinator's first name
     * @param lastName Staff coordinator's last name
     * @param email Staff coordinator's email
     * @param phoneNumber Staff coordinator's phone number
     * @param password Staff coordinator's password
     * @return Created staff coordinator
     */
    public User createStaffCoordinator(String firstName, String lastName, String email, String phoneNumber, String password) {
        // Store password as plain text
        User staffCoordinator = new User(firstName, lastName, email, phoneNumber, password, "STAFF_COORDINATOR");
        return userRepository.save(staffCoordinator);
    }
    
    /**
     * Create a new receptionist
     * @param firstName Receptionist's first name
     * @param lastName Receptionist's last name
     * @param email Receptionist's email
     * @param phoneNumber Receptionist's phone number
     * @param password Receptionist's password
     * @return Created receptionist
     */
    public User createReceptionist(String firstName, String lastName, String email, String phoneNumber, String password) {
        // Store password as plain text
        User receptionist = new User(firstName, lastName, email, phoneNumber, password, "RECEPTIONIST");
        return userRepository.save(receptionist);
    }
    
    /**
     * Create a new senior medical officer
     * @param firstName SMO's first name
     * @param lastName SMO's last name
     * @param email SMO's email
     * @param phoneNumber SMO's phone number
     * @param password SMO's password
     * @return Created senior medical officer
     */
    public User createSeniorMedicalOfficer(String firstName, String lastName, String email, String phoneNumber, String password) {
        // Store password as plain text
        User smo = new User(firstName, lastName, email, phoneNumber, password, "SENIOR_MEDICAL_OFFICER");
        return userRepository.save(smo);
    }
    
    /**
     * Create a new customer support manager
     * @param firstName Customer Support Manager's first name
     * @param lastName Customer Support Manager's last name
     * @param email Customer Support Manager's email
     * @param phoneNumber Customer Support Manager's phone number
     * @param password Customer Support Manager's password
     * @return Created customer support manager
     */
    public User createCustomerSupportManager(String firstName, String lastName, String email, String phoneNumber, String password) {
        // Store password as plain text
        User customerSupportManager = new User(firstName, lastName, email, phoneNumber, password, "CUSTOMER_SUPPORT_MANAGER");
        return userRepository.save(customerSupportManager);
    }
    
    /**
     * Update user profile information
     * @param email User's email (used to identify the user)
     * @param firstName Updated first name
     * @param lastName Updated last name
     * @param phoneNumber Updated phone number
     * @return Updated user
     * @throws IllegalArgumentException if user not found
     */
    public User updateUserProfile(String email, String firstName, String lastName, String phoneNumber) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        
        User user = userOpt.get();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        
        return userRepository.save(user);
    }
    
    /**
     * Update user password
     * @param email User's email (used to identify the user)
     * @param currentPassword Current password for verification
     * @param newPassword New password
     * @return Updated user
     * @throws IllegalArgumentException if user not found or current password is incorrect
     */
    public User updateUserPassword(String email, String currentPassword, String newPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        
        User user = userOpt.get();
        
        // Verify current password
        if (!verifyPassword(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        // Set new password as plain text
        user.setPassword(newPassword);
        
        return userRepository.save(user);
    }
    
}