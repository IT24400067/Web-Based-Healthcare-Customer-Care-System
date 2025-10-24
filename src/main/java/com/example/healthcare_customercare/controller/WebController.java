package com.example.healthcare_customercare.controller;

import com.example.healthcare_customercare.entity.User;
import com.example.healthcare_customercare.entity.Doctor;
import com.example.healthcare_customercare.entity.DoctorSchedule;
import com.example.healthcare_customercare.entity.Appointment;
import com.example.healthcare_customercare.entity.Feedback;
import com.example.healthcare_customercare.entity.SupportTicket;
import com.example.healthcare_customercare.service.UserService;
import com.example.healthcare_customercare.service.DoctorService;
import com.example.healthcare_customercare.service.RoomService;
import com.example.healthcare_customercare.service.DoctorScheduleService;
import com.example.healthcare_customercare.service.AppointmentService;
import com.example.healthcare_customercare.service.FeedbackService;
import com.example.healthcare_customercare.service.SupportTicketService;
import com.example.healthcare_customercare.service.PdfExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Controller
public class WebController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private DoctorScheduleService doctorScheduleService;
    
    @Autowired
    private PdfExportService pdfExportService;
    
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private FeedbackService feedbackService;
    
    @Autowired
    private SupportTicketService supportTicketService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signup.html")
    public String signup() {
        return "signup";
    }

    @GetMapping("/login.html")
    public String login() {
        return "login";
    }

    @GetMapping("/appointment.html")
    public String appointment(@RequestParam(required = false) String email, Model model, HttpSession session) {
        // Add doctors to the model for the appointment page
        model.addAttribute("doctors", doctorService.getAllDoctors());
        
        User customer = null;
        
        // First, try to get customer from session
        customer = (User) session.getAttribute("loggedInUser");
        
        // If not in session, try to get from URL parameter
        if (customer == null && email != null && !email.isEmpty()) {
            Optional<User> userOpt = userService.findUserByEmail(email);
            if (userOpt.isPresent()) {
                customer = userOpt.get();
                // Store in session for future requests
                session.setAttribute("loggedInUser", customer);
            }
        }
        
        // If still no customer found, create a mock customer
        if (customer == null) {
            customer = new User();
            customer.setFirstName("");
            customer.setLastName("");
            customer.setEmail("");
            customer.setPhoneNumber("");
        }
        
        model.addAttribute("customer", customer);
        return "Customer/appointment";
    }

    @GetMapping("/feedback.html")
    public String feedback(@RequestParam(required = false) String email, Model model, HttpSession session) {
        User customer = null;
        
        // First, try to get customer from session
        customer = (User) session.getAttribute("loggedInUser");
        
        // If not in session, try to get from URL parameter
        if (customer == null && email != null && !email.isEmpty()) {
            Optional<User> userOpt = userService.findUserByEmail(email);
            if (userOpt.isPresent()) {
                customer = userOpt.get();
                // Store in session for future requests
                session.setAttribute("loggedInUser", customer);
            }
        }
        
        // If still no customer found, create a mock customer
        if (customer == null) {
            customer = new User();
            customer.setFirstName("");
            customer.setLastName("");
            customer.setEmail("");
            customer.setPhoneNumber("");
        }
        
        model.addAttribute("customer", customer);
        return "Customer/feedback";
    }

    @GetMapping("/support-ticket.html")
    public String supportTicket(@RequestParam(required = false) String email, Model model, HttpSession session) {
        User customer = null;
        
        // First, try to get customer from session
        customer = (User) session.getAttribute("loggedInUser");
        
        // If not in session, try to get from URL parameter
        if (customer == null && email != null && !email.isEmpty()) {
            Optional<User> userOpt = userService.findUserByEmail(email);
            if (userOpt.isPresent()) {
                customer = userOpt.get();
                // Store in session for future requests
                session.setAttribute("loggedInUser", customer);
            }
        }
        
        // If still no customer found, create a mock customer
        if (customer == null) {
            customer = new User();
            customer.setFirstName("");
            customer.setLastName("");
            customer.setEmail("");
            customer.setPhoneNumber("");
        }
        
        model.addAttribute("customer", customer);
        return "Customer/support-ticket";
    }

    @GetMapping("/customer.html")
    public String customer(@RequestParam(required = false) String email, Model model, HttpSession session) {
        User customer = null;
        
        // First, try to get customer from session
        customer = (User) session.getAttribute("loggedInUser");
        
        // If not in session, try to get from URL parameter
        if (customer == null && email != null && !email.isEmpty()) {
            Optional<User> customerOpt = userService.findCustomerByEmail(email);
            if (customerOpt.isPresent()) {
                customer = customerOpt.get();
                // Store in session for future requests
                session.setAttribute("loggedInUser", customer);
            }
        }
        
        // If still no customer found, create a mock customer
        if (customer == null) {
            customer = new User();
            customer.setFirstName("");
            customer.setLastName("");
            customer.setEmail("");
            customer.setPhoneNumber("");
        }
        
        model.addAttribute("customer", customer);
        return "Customer/customer";
    }

    @GetMapping("/staff-coordinator.html")
    public String staffCoordinator(@RequestParam(required = false) String email, Model model) {
        if (email != null && !email.isEmpty()) {
            // Find the staff coordinator by email and pass to the view
            Optional<User> staffCoordinator = userService.findUserByEmail(email);
            if (staffCoordinator.isPresent() && "STAFF_COORDINATOR".equals(staffCoordinator.get().getRole())) {
                model.addAttribute("staffCoordinator", staffCoordinator.get());
            } else {
                // Fallback to mock staff coordinator if not found
                User mockStaffCoordinator = new User();
                mockStaffCoordinator.setFirstName("Sarah");
                mockStaffCoordinator.setLastName("Johnson");
                mockStaffCoordinator.setEmail(email);
                mockStaffCoordinator.setRole("STAFF_COORDINATOR");
                model.addAttribute("staffCoordinator", mockStaffCoordinator);
            }
        } else {
            // Fallback to mock staff coordinator if no email provided
            User mockStaffCoordinator = new User();
            mockStaffCoordinator.setFirstName("Sarah");
            mockStaffCoordinator.setLastName("Johnson");
            mockStaffCoordinator.setEmail("coordinator@arogya.com");
            mockStaffCoordinator.setRole("STAFF_COORDINATOR");
            model.addAttribute("staffCoordinator", mockStaffCoordinator);
        }
        
        // Add doctors from database, rooms, and schedules
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("rooms", roomService.getAvailableRooms());
        model.addAttribute("schedules", java.util.Arrays.asList("Morning (8:00 AM - 12:00 PM)", "Afternoon (1:00 PM - 5:00 PM)", "Evening (6:00 PM - 10:00 PM)"));
        
        // Add metrics data for the dashboard
        try {
            // Get current assignments count (doctors assigned to rooms)
            int currentAssignments = doctorService.getAllDoctors().size(); // Simplified for now
            
            // Get scheduled appointments count for today
            java.time.LocalDate today = java.time.LocalDate.now();
            List<Appointment> todayAppointments = appointmentService.getAppointmentsByDate(today);
            int scheduledAppointments = todayAppointments.size();
            
            // Calculate system status (simplified)
            int totalDoctors = doctorService.getAllDoctors().size();
            int availableRooms = roomService.getAvailableRooms().size();
            int systemStatus = totalDoctors > 0 && availableRooms > 0 ? 100 : 75;
            
            model.addAttribute("currentAssignments", currentAssignments);
            model.addAttribute("scheduledAppointments", scheduledAppointments);
            model.addAttribute("systemStatus", systemStatus);
            
        } catch (Exception e) {
            // Fallback values if there's any error
            model.addAttribute("currentAssignments", 8);
            model.addAttribute("scheduledAppointments", 24);
            model.addAttribute("systemStatus", 100);
            System.err.println("Error loading metrics: " + e.getMessage());
        }
        
        return "Staff-Coordinator/staff-coordinator";
    }

    @GetMapping("/staff-coordinator")
    public String staffCoordinatorAlt(@RequestParam(required = false) String email, Model model) {
        // Redirect to the .html version for consistency
        return "redirect:/staff-coordinator.html" + (email != null ? "?email=" + email : "");
    }

    // Staff Coordinator separate pages
    @GetMapping("/staff-coordinator/doctors/management")
    public String doctorManagement(@RequestParam(required = false) String email, Model model) {
        // Get staff coordinator info
        if (email != null && !email.isEmpty()) {
            Optional<User> staffCoordinator = userService.findUserByEmail(email);
            if (staffCoordinator.isPresent() && "STAFF_COORDINATOR".equals(staffCoordinator.get().getRole())) {
                model.addAttribute("staffCoordinator", staffCoordinator.get());
            } else {
                User mockStaffCoordinator = new User();
                mockStaffCoordinator.setFirstName("Sarah");
                mockStaffCoordinator.setLastName("Johnson");
                mockStaffCoordinator.setEmail(email);
                mockStaffCoordinator.setRole("STAFF_COORDINATOR");
                model.addAttribute("staffCoordinator", mockStaffCoordinator);
            }
        } else {
            User mockStaffCoordinator = new User();
            mockStaffCoordinator.setFirstName("Sarah");
            mockStaffCoordinator.setLastName("Johnson");
            mockStaffCoordinator.setEmail("coordinator@arogya.com");
            mockStaffCoordinator.setRole("STAFF_COORDINATOR");
            model.addAttribute("staffCoordinator", mockStaffCoordinator);
        }
        
        // Add doctors from database
        model.addAttribute("doctors", doctorService.getAllDoctors());
        
        return "Staff-Coordinator/doctor-management";
    }

    @GetMapping("/staff-coordinator/doctors/assignment")
    public String doctorAssignment(@RequestParam(required = false) String email, Model model) {
        // Get staff coordinator info
        if (email != null && !email.isEmpty()) {
            Optional<User> staffCoordinator = userService.findUserByEmail(email);
            if (staffCoordinator.isPresent() && "STAFF_COORDINATOR".equals(staffCoordinator.get().getRole())) {
                model.addAttribute("staffCoordinator", staffCoordinator.get());
            } else {
                User mockStaffCoordinator = new User();
                mockStaffCoordinator.setFirstName("Sarah");
                mockStaffCoordinator.setLastName("Johnson");
                mockStaffCoordinator.setEmail(email);
                mockStaffCoordinator.setRole("STAFF_COORDINATOR");
                model.addAttribute("staffCoordinator", mockStaffCoordinator);
            }
        } else {
            User mockStaffCoordinator = new User();
            mockStaffCoordinator.setFirstName("Sarah");
            mockStaffCoordinator.setLastName("Johnson");
            mockStaffCoordinator.setEmail("coordinator@arogya.com");
            mockStaffCoordinator.setRole("STAFF_COORDINATOR");
            model.addAttribute("staffCoordinator", mockStaffCoordinator);
        }
        
        // Add doctors from database, rooms, and schedules
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("rooms", roomService.getAvailableRooms());
        model.addAttribute("schedules", java.util.Arrays.asList("Morning (8:00 AM - 12:00 PM)", "Afternoon (1:00 PM - 5:00 PM)", "Evening (6:00 PM - 10:00 PM)"));
        
        return "Staff-Coordinator/doctor-assignment";
    }


    @GetMapping("/staff-coordinator/schedule")
    public String scheduleManagement(@RequestParam(required = false) String email, Model model) {
        // Get staff coordinator info
        if (email != null && !email.isEmpty()) {
            Optional<User> staffCoordinator = userService.findUserByEmail(email);
            if (staffCoordinator.isPresent() && "STAFF_COORDINATOR".equals(staffCoordinator.get().getRole())) {
                model.addAttribute("staffCoordinator", staffCoordinator.get());
            } else {
                User mockStaffCoordinator = new User();
                mockStaffCoordinator.setFirstName("Sarah");
                mockStaffCoordinator.setLastName("Johnson");
                mockStaffCoordinator.setEmail(email);
                mockStaffCoordinator.setRole("STAFF_COORDINATOR");
                model.addAttribute("staffCoordinator", mockStaffCoordinator);
            }
        } else {
            User mockStaffCoordinator = new User();
            mockStaffCoordinator.setFirstName("Sarah");
            mockStaffCoordinator.setLastName("Johnson");
            mockStaffCoordinator.setEmail("coordinator@arogya.com");
            mockStaffCoordinator.setRole("STAFF_COORDINATOR");
            model.addAttribute("staffCoordinator", mockStaffCoordinator);
        }
        
        // Add doctors from database, rooms, and schedule statistics
        model.addAttribute("doctors", doctorService.getAllDoctors());
        model.addAttribute("rooms", roomService.getAvailableRooms());
        
        // Calculate actual schedule statistics
        try {
            List<DoctorSchedule> allSchedules = doctorScheduleService.getAllSchedules();
            long totalSchedules = allSchedules.size();
            long availableSchedules = allSchedules.stream().filter(s -> s.getIsAvailable()).count();
            long busySchedules = allSchedules.stream().filter(s -> !s.getIsAvailable()).count();
            long totalDoctors = doctorService.getAllDoctors().size();
            
            model.addAttribute("totalSchedules", totalSchedules);
            model.addAttribute("availableSchedules", availableSchedules);
            model.addAttribute("busySchedules", busySchedules);
            model.addAttribute("totalDoctors", totalDoctors);
        } catch (Exception e) {
            // Fallback to default values if there's an error
            model.addAttribute("totalSchedules", 0);
            model.addAttribute("availableSchedules", 0);
            model.addAttribute("busySchedules", 0);
            model.addAttribute("totalDoctors", 0);
        }
        
        return "Staff-Coordinator/schedule-management";
    }

    @GetMapping("/receptionist.html")
    public String receptionist(@RequestParam(required = false) String email, Model model) {
        if (email != null && !email.isEmpty()) {
            // Find the receptionist by email and pass to the view
            Optional<User> receptionist = userService.findUserByEmail(email);
            if (receptionist.isPresent() && "RECEPTIONIST".equals(receptionist.get().getRole())) {
                model.addAttribute("receptionist", receptionist.get());
            } else {
                // Fallback to mock receptionist if not found
                User mockReceptionist = new User();
                mockReceptionist.setFirstName("Emma");
                mockReceptionist.setLastName("Wilson");
                mockReceptionist.setEmail(email);
                mockReceptionist.setRole("RECEPTIONIST");
                model.addAttribute("receptionist", mockReceptionist);
            }
        } else {
            // Fallback to mock receptionist if no email provided
            User mockReceptionist = new User();
            mockReceptionist.setFirstName("Emma");
            mockReceptionist.setLastName("Wilson");
            mockReceptionist.setEmail("receptionist@arogya.com");
            mockReceptionist.setRole("RECEPTIONIST");
            model.addAttribute("receptionist", mockReceptionist);
        }
        
        // Add metrics data for the dashboard
        try {
            // Get today's appointments count
            java.time.LocalDate today = java.time.LocalDate.now();
            List<Appointment> todayAppointments = appointmentService.getAppointmentsByDate(today);
            int todaysAppointments = todayAppointments.size();
            
            // Get pending appointments count (appointments that need confirmation)
            List<Appointment> allAppointments = appointmentService.getAllAppointments();
            int pendingAppointments = allAppointments.size(); // Simplified for now
            
            // Get total patients count (users with CUSTOMER role)
            List<User> customers = userService.findByRole("CUSTOMER");
            int totalPatients = customers.size();
            
            // Get available doctors count
            List<com.example.healthcare_customercare.entity.Doctor> doctors = doctorService.getAllDoctors();
            int availableDoctors = doctors.size(); // Simplified for now
            
            model.addAttribute("todaysAppointments", todaysAppointments);
            model.addAttribute("pendingAppointments", pendingAppointments);
            model.addAttribute("totalPatients", totalPatients);
            model.addAttribute("availableDoctors", availableDoctors);
            
        } catch (Exception e) {
            // Fallback values if there's any error
            model.addAttribute("todaysAppointments", 12);
            model.addAttribute("pendingAppointments", 5);
            model.addAttribute("totalPatients", 150);
            model.addAttribute("availableDoctors", 8);
            System.err.println("Error loading receptionist metrics: " + e.getMessage());
        }
        
        return "Receptionist/receptionist";
    }

    @GetMapping("/receptionist")
    public String receptionistAlt(@RequestParam(required = false) String email, Model model) {
        // Redirect to the .html version for consistency
        return "redirect:/receptionist.html" + (email != null ? "?email=" + email : "");
    }

    @GetMapping("/senior-medical-officer.html")
    public String seniorMedicalOfficer(@RequestParam(required = false) String email, Model model) {
        if (email != null && !email.isEmpty()) {
            // Find the senior medical officer by email and pass to the view
            Optional<User> seniorMedicalOfficer = userService.findUserByEmail(email);
            if (seniorMedicalOfficer.isPresent() && "SENIOR_MEDICAL_OFFICER".equals(seniorMedicalOfficer.get().getRole())) {
                model.addAttribute("seniorMedicalOfficer", seniorMedicalOfficer.get());
            } else {
                // Fallback to mock senior medical officer if not found
                User mockSMO = new User();
                mockSMO.setFirstName("Dr. Sarah");
                mockSMO.setLastName("Johnson");
                mockSMO.setEmail(email);
                mockSMO.setRole("SENIOR_MEDICAL_OFFICER");
                model.addAttribute("seniorMedicalOfficer", mockSMO);
            }
        } else {
            // Fallback to mock senior medical officer if no email provided
            User mockSMO = new User();
            mockSMO.setFirstName("Dr. Sarah");
            mockSMO.setLastName("Johnson");
            mockSMO.setEmail("smo@arogya.com");
            mockSMO.setRole("SENIOR_MEDICAL_OFFICER");
            model.addAttribute("seniorMedicalOfficer", mockSMO);
        }
        
        // Add metrics data for the dashboard
        try {
            // Get today's consultations count
            java.time.LocalDate today = java.time.LocalDate.now();
            List<Appointment> todayAppointments = appointmentService.getAppointmentsByDate(today);
            model.addAttribute("todaysConsultations", todayAppointments.size());
            
            // Get active medical staff count (doctors)
            List<com.example.healthcare_customercare.entity.Doctor> allDoctors = doctorService.getAllDoctors();
            model.addAttribute("activeMedicalStaff", allDoctors.size());
            
            // Get pending reviews count (appointments pending approval)
            // For now, we'll use a simple count of appointments as pending reviews
            model.addAttribute("pendingReviews", Math.max(0, todayAppointments.size() / 4));
            
            // Get critical cases count (high priority appointments)
            // For now, we'll use a simple count of appointments as critical cases
            model.addAttribute("criticalCases", Math.max(0, todayAppointments.size() / 10));
            
        } catch (Exception e) {
            // Set default values if there's an error
            model.addAttribute("todaysConsultations", 0);
            model.addAttribute("activeMedicalStaff", 0);
            model.addAttribute("pendingReviews", 0);
            model.addAttribute("criticalCases", 0);
            System.err.println("Error loading senior medical officer metrics: " + e.getMessage());
        }
        
        return "Senior Medical Officer/senior medical officer";
    }

    @GetMapping("/senior-medical-officer")
    public String seniorMedicalOfficerAlt(@RequestParam(required = false) String email, Model model) {
        // Redirect to the .html version for consistency
        return "redirect:/senior-medical-officer.html" + (email != null ? "?email=" + email : "");
    }

    @GetMapping("/senior-medical-officer/patient-history")
    public String patientHistory(@RequestParam(required = false) String email, Model model) {
        if (email == null || email.isEmpty()) {
            model.addAttribute("error", "Patient email is required");
            return "redirect:/senior-medical-officer.html";
        }
        
        // Validate that the email parameter is provided
        model.addAttribute("patientEmail", email);
        return "Senior Medical Officer/patient-history";
    }


    @PostMapping("/signup")
    public String signupSubmit(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phoneNumber,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {
        
        // Basic validation
        if (firstName == null || firstName.trim().isEmpty()) {
            model.addAttribute("error", "First name is required");
            return "signup";
        }
        
        if (lastName == null || lastName.trim().isEmpty()) {
            model.addAttribute("error", "Last name is required");
            return "signup";
        }
        
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            model.addAttribute("error", "Valid email is required");
            return "signup";
        }
        
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            model.addAttribute("error", "Phone number is required");
            return "signup";
        }
        
        if (password == null || password.length() < 8) {
            model.addAttribute("error", "Password must be at least 8 characters");
            return "signup";
        }
        
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "signup";
        }
        
        // Check if email already exists
        if (userService.emailExists(email)) {
            model.addAttribute("error", "Email already exists. Please use a different email.");
            return "signup";
        }
        
        try {
            // Create new customer
            userService.createCustomer(firstName, lastName, email, phoneNumber, password);
            
            // If successful, redirect to login page
            return "redirect:/login.html";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating account. Please try again.");
            return "signup";
        }
    }

    @PostMapping("/login")
    public String loginSubmit(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(required = false) String rememberMe,
            Model model,
            HttpSession session) {
        
        // Basic validation
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            model.addAttribute("error", "Valid email is required");
            return "login";
        }
        
        if (password == null || password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters");
            return "login";
        }
        
        try {
            System.out.println("=== LOGIN DEBUG ===");
            System.out.println("Email: " + email);
            System.out.println("Password length: " + (password != null ? password.length() : "null"));
            
            // Authenticate user against database (check all roles)
            Optional<User> user = userService.authenticateUser(email, password);
            
            System.out.println("User found: " + user.isPresent());
            if (user.isPresent()) {
                User authenticatedUser = user.get();
                System.out.println("User role: " + authenticatedUser.getRole());
                System.out.println("User email: " + authenticatedUser.getEmail());
                System.out.println("User name: " + authenticatedUser.getFullName());
            }
            
            if (user.isPresent()) {
                User authenticatedUser = user.get();
                
                // Store user in session for persistence across page refreshes
                session.setAttribute("loggedInUser", authenticatedUser);
                
                // Check user role and redirect accordingly
                if ("CUSTOMER".equals(authenticatedUser.getRole())) {
                    // Redirect to customer dashboard
                    return "redirect:/customer.html?success=login_successful&email=" + email;
                } else if ("STAFF_COORDINATOR".equals(authenticatedUser.getRole())) {
                    // Redirect to staff coordinator dashboard
                    return "redirect:/staff-coordinator.html?success=login_successful&email=" + email;
                } else if ("RECEPTIONIST".equals(authenticatedUser.getRole())) {
                    // Redirect to receptionist dashboard
                    return "redirect:/receptionist.html?success=login_successful&email=" + email;
                } else if ("SENIOR_MEDICAL_OFFICER".equals(authenticatedUser.getRole())) {
                    // Redirect to senior medical officer dashboard
                    return "redirect:/senior-medical-officer.html?success=login_successful&email=" + email;
                } else if ("CUSTOMER_SUPPORT_MANAGER".equals(authenticatedUser.getRole())) {
                    // Redirect to customer support manager dashboard
                    return "redirect:/customer-support-manager.html?success=login_successful&email=" + email;
                } else if ("ADMIN".equals(authenticatedUser.getRole())) {
                    // Redirect to system admin dashboard
                    return "redirect:/system-admin/dashboard?success=login_successful&email=" + email;
                } else {
                    model.addAttribute("error", "Unknown user role");
                    return "login";
                }
            } else {
                // Check if email exists but password is wrong
                Optional<User> existingUser = userService.findUserByEmail(email);
                if (existingUser.isPresent()) {
                    model.addAttribute("error", "Invalid password");
                } else {
                    model.addAttribute("error", "No account found with this email address");
                }
                return "login";
            }
        } catch (Exception e) {
            System.err.println("=== LOGIN ERROR ===");
            e.printStackTrace();
            model.addAttribute("error", "Login error: " + e.getMessage());
            return "login";
        }
    }
    
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        // Clear the session
        session.invalidate();
        return "redirect:/login.html?success=logged_out";
    }
    
    // API endpoint to get booked appointments for a patient
    @GetMapping("/api/appointments/patient")
    @ResponseBody
    public Map<String, Object> getPatientAppointments(@RequestParam String email, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("=== DEBUG: getPatientAppointments called ===");
            System.out.println("Email parameter: " + email);
            
            // Get user from session first, then fallback to email parameter
            User user = (User) session.getAttribute("loggedInUser");
            System.out.println("User from session: " + (user != null ? user.getEmail() : "null"));
            
            String patientEmail = (user != null && user.getEmail() != null) ? user.getEmail() : email;
            System.out.println("Final patient email: " + patientEmail);
            
            if (patientEmail == null || patientEmail.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Patient email is required");
                return response;
            }
            
            // Get appointments for the patient
            List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientEmail);
            System.out.println("Found " + appointments.size() + " appointments for email: " + patientEmail);
            
            // Debug: Print all appointments
            for (Appointment apt : appointments) {
                System.out.println("Appointment: " + apt);
            }
            
            // Enrich appointments with doctor information
            List<Map<String, Object>> enrichedAppointments = new java.util.ArrayList<>();
            for (Appointment appointment : appointments) {
                Map<String, Object> appointmentData = new HashMap<>();
                appointmentData.put("id", appointment.getId());
                appointmentData.put("patientName", appointment.getPatientName());
                appointmentData.put("appointmentDate", appointment.getAppointmentDate());
                appointmentData.put("docId", appointment.getDocId());
                appointmentData.put("roomId", appointment.getRoomId());
                appointmentData.put("scheduleId", appointment.getScheduleId());
                
                // Get doctor information
                try {
                    Optional<com.example.healthcare_customercare.entity.Doctor> doctorOpt = doctorService.getDoctorById(appointment.getDocId());
                    if (doctorOpt.isPresent()) {
                        com.example.healthcare_customercare.entity.Doctor doctor = doctorOpt.get();
                        appointmentData.put("doctorName", doctor.getDoctorName());
                        appointmentData.put("specialization", doctor.getSpecialization());
                        appointmentData.put("department", doctor.getDepartment());
                        System.out.println("Found doctor: " + doctor.getDoctorName());
                    } else {
                        appointmentData.put("doctorName", "Unknown Doctor");
                        appointmentData.put("specialization", "Unknown");
                        appointmentData.put("department", "Unknown");
                        System.out.println("Doctor not found for ID: " + appointment.getDocId());
                    }
                } catch (Exception e) {
                    appointmentData.put("doctorName", "Unknown Doctor");
                    appointmentData.put("specialization", "Unknown");
                    appointmentData.put("department", "Unknown");
                    System.out.println("Error getting doctor: " + e.getMessage());
                }
                
                enrichedAppointments.add(appointmentData);
            }
            
            response.put("success", true);
            response.put("appointments", enrichedAppointments);
            response.put("count", appointments.size());
            
            System.out.println("=== DEBUG: Returning response ===");
            System.out.println("Success: " + response.get("success"));
            System.out.println("Count: " + response.get("count"));
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching appointments: " + e.getMessage());
            System.err.println("Error fetching appointments for email " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }
    
    // Debug endpoint to check all appointments in database
    @GetMapping("/api/appointments/debug")
    @ResponseBody
    public Map<String, Object> debugAllAppointments() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Appointment> allAppointments = appointmentService.getAllAppointments();
            System.out.println("=== DEBUG: All appointments in database ===");
            System.out.println("Total appointments: " + allAppointments.size());
            
            List<Map<String, Object>> appointmentList = new java.util.ArrayList<>();
            for (Appointment apt : allAppointments) {
                Map<String, Object> aptData = new HashMap<>();
                aptData.put("id", apt.getId());
                aptData.put("patientName", apt.getPatientName());
                aptData.put("patientEmail", apt.getPatientEmail());
                aptData.put("appointmentDate", apt.getAppointmentDate());
                aptData.put("docId", apt.getDocId());
                aptData.put("roomId", apt.getRoomId());
                aptData.put("scheduleId", apt.getScheduleId());
                appointmentList.add(aptData);
                System.out.println("Appointment: " + apt);
            }
            
            response.put("success", true);
            response.put("totalCount", allAppointments.size());
            response.put("appointments", appointmentList);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }
    
    // API endpoint to get feedback history for a patient
    @GetMapping("/api/feedback/history")
    @ResponseBody
    public Map<String, Object> getFeedbackHistory(@RequestParam String email, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("=== DEBUG: getFeedbackHistory called ===");
            System.out.println("Email parameter: " + email);
            
            // Get user from session first, then fallback to email parameter
            User user = (User) session.getAttribute("loggedInUser");
            String userEmail = (user != null && user.getEmail() != null) ? user.getEmail() : email;
            System.out.println("Final user email: " + userEmail);
            
            if (userEmail == null || userEmail.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "User email is required");
                return response;
            }
            
            // Get feedback for the user
            List<Feedback> feedbackList = feedbackService.getFeedbackByUserEmail(userEmail);
            System.out.println("Found " + feedbackList.size() + " feedback entries for email: " + userEmail);
            
            // Convert to response format
            List<Map<String, Object>> feedbackData = new java.util.ArrayList<>();
            for (Feedback feedback : feedbackList) {
                Map<String, Object> feedbackItem = new HashMap<>();
                feedbackItem.put("feedbackId", feedback.getFeedbackId());
                feedbackItem.put("feedbackType", feedback.getFeedbackType());
                feedbackItem.put("message", feedback.getMessage());
                feedbackItem.put("rating", feedback.getRating());
                feedbackItem.put("createdAt", feedback.getCreatedAt());
                feedbackItem.put("userEmail", feedback.getUserEmail());
                feedbackData.add(feedbackItem);
            }
            
            response.put("success", true);
            response.put("feedback", feedbackData);
            response.put("count", feedbackList.size());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching feedback: " + e.getMessage());
            System.err.println("Error fetching feedback for email " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }
    
    // API endpoint to get support tickets for a patient
    @GetMapping("/api/support-tickets/patient")
    @ResponseBody
    public Map<String, Object> getPatientSupportTickets(@RequestParam String email, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("=== DEBUG: getPatientSupportTickets called ===");
            System.out.println("Email parameter: " + email);
            
            // Get user from session first, then fallback to email parameter
            User user = (User) session.getAttribute("loggedInUser");
            String userEmail = (user != null && user.getEmail() != null) ? user.getEmail() : email;
            System.out.println("Final user email: " + userEmail);
            
            if (userEmail == null || userEmail.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "User email is required");
                return response;
            }
            
            // Get support tickets for the user
            List<SupportTicket> tickets = supportTicketService.getTicketsByUserEmail(userEmail);
            System.out.println("Found " + tickets.size() + " support tickets for email: " + userEmail);
            
            // Convert to response format
            List<Map<String, Object>> ticketData = new java.util.ArrayList<>();
            for (SupportTicket ticket : tickets) {
                Map<String, Object> ticketItem = new HashMap<>();
                ticketItem.put("ticketId", ticket.getTicketId());
                ticketItem.put("category", ticket.getCategory());
                ticketItem.put("priority", ticket.getPriority());
                ticketItem.put("subject", ticket.getSubject());
                ticketItem.put("description", ticket.getDescription());
                ticketItem.put("status", ticket.getStatus());
                ticketItem.put("createdAt", ticket.getCreatedAt());
                ticketItem.put("userEmail", ticket.getUserEmail());
                ticketItem.put("reply", ticket.getReply());
                ticketItem.put("replyDate", ticket.getReplyDate());
                ticketItem.put("updatedAt", ticket.getUpdatedAt());
                ticketData.add(ticketItem);
            }
            
            response.put("success", true);
            response.put("tickets", ticketData);
            response.put("count", tickets.size());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error fetching support tickets: " + e.getMessage());
            System.err.println("Error fetching support tickets for email " + email + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        return response;
    }
    
    // Doctor Management Endpoints
    @PostMapping("/staff-coordinator/doctors/add")
    public String addDoctor(
            @RequestParam String doctorName,
            @RequestParam String specialization,
            @RequestParam String department,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String staffCoordinatorEmail,
            Model model) {
        
        // Basic validation
        if (doctorName == null || doctorName.trim().isEmpty()) {
            return "redirect:/staff-coordinator/doctors/management?error=doctor_name_required" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
        
        if (specialization == null || specialization.trim().isEmpty()) {
            return "redirect:/staff-coordinator/doctors/management?error=specialization_required" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
        
        if (department == null || department.trim().isEmpty()) {
            return "redirect:/staff-coordinator/doctors/management?error=department_required" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
        
        try {
            // Add doctor to database
            doctorService.addDoctor(doctorName.trim(), specialization.trim(), department.trim(), email != null ? email.trim() : null);
            
            // Redirect with success message
            return "redirect:/staff-coordinator/doctors/management?success=doctor_added" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        } catch (IllegalArgumentException e) {
            // Doctor already exists
            return "redirect:/staff-coordinator/doctors/management?error=doctor_exists" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        } catch (Exception e) {
            // Other errors
            return "redirect:/staff-coordinator/doctors/management?error=add_doctor_failed" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
    }
    
    @PostMapping("/staff-coordinator/doctors/update")
    public String updateDoctor(
            @RequestParam Long docId,
            @RequestParam String doctorName,
            @RequestParam String specialization,
            @RequestParam String department,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String staffCoordinatorEmail,
            Model model) {
        
        // Basic validation
        if (docId == null) {
            return "redirect:/staff-coordinator/doctors/management?error=doctor_id_required" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
        
        if (doctorName == null || doctorName.trim().isEmpty()) {
            return "redirect:/staff-coordinator/doctors/management?error=doctor_name_required" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
        
        if (specialization == null || specialization.trim().isEmpty()) {
            return "redirect:/staff-coordinator/doctors/management?error=specialization_required" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
        
        if (department == null || department.trim().isEmpty()) {
            return "redirect:/staff-coordinator/doctors/management?error=department_required" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
        
        try {
            // Update doctor in database
            doctorService.updateDoctor(docId, doctorName.trim(), specialization.trim(), department.trim(), email != null ? email.trim() : null);
            
            // Redirect with success message
            return "redirect:/staff-coordinator/doctors/management?success=doctor_updated" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        } catch (IllegalArgumentException e) {
            // Doctor not found
            return "redirect:/staff-coordinator/doctors/management?error=doctor_not_found" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        } catch (Exception e) {
            // Other errors
            return "redirect:/staff-coordinator/doctors/management?error=update_doctor_failed" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
    }
    
    @PostMapping("/staff-coordinator/doctors/delete")
    public String deleteDoctor(
            @RequestParam Long docId,
            @RequestParam(required = false) String staffCoordinatorEmail,
            Model model) {
        
        // Basic validation
        if (docId == null) {
            return "redirect:/staff-coordinator/doctors/management?error=doctor_id_required" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
        
        try {
            // Delete doctor from database
            boolean deleted = doctorService.deleteDoctor(docId);
            
            if (deleted) {
                // Redirect with success message
                return "redirect:/staff-coordinator/doctors/management?success=doctor_deleted" + 
                       (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
            } else {
                // Doctor not found or deletion failed
                return "redirect:/staff-coordinator/doctors/management?error=doctor_not_found" + 
                       (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
            }
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error in deleteDoctor controller: " + e.getMessage());
            e.printStackTrace();
            
            // Other errors
            return "redirect:/staff-coordinator/doctors/management?error=delete_doctor_failed" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
    }
    
    // Doctor Assignment Endpoint
    @PostMapping("/staff-coordinator/doctors/assign")
    public String assignDoctorToRoom(
            @RequestParam Long doctorId,
            @RequestParam Long roomId,
            @RequestParam String timePeriod,
            @RequestParam String[] availableDates,
            @RequestParam(required = false) String staffCoordinatorEmail,
            Model model) {
        
        // Basic validation
        if (doctorId == null) {
            return "redirect:/staff-coordinator/doctors/assignment?error=doctor_required" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
        
        if (roomId == null) {
            return "redirect:/staff-coordinator/doctors/assignment?error=room_required" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
        
        if (timePeriod == null || timePeriod.trim().isEmpty()) {
            return "redirect:/staff-coordinator/doctors/assignment?error=time_period_required" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
        
        if (availableDates == null || availableDates.length == 0) {
            return "redirect:/staff-coordinator/doctors/assignment?error=dates_required" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
        
        try {
            // Extract time period name from the full description
            String periodName;
            if (timePeriod.contains("Morning")) {
                periodName = "Morning";
            } else if (timePeriod.contains("Afternoon")) {
                periodName = "Afternoon";
            } else if (timePeriod.contains("Evening")) {
                periodName = "Evening";
            } else {
                periodName = timePeriod;
            }
            
            // Validate that doctor exists
            if (!doctorService.existsById(doctorId)) {
                return "redirect:/staff-coordinator/doctors/assignment?error=assignment_failed&details=" + 
                       java.net.URLEncoder.encode("Doctor with ID " + doctorId + " not found", java.nio.charset.StandardCharsets.UTF_8) + 
                       (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
            }
            
            // Validate that room exists
            if (!roomService.getRoomById(roomId).isPresent()) {
                return "redirect:/staff-coordinator/doctors/assignment?error=assignment_failed&details=" + 
                       java.net.URLEncoder.encode("Room with ID " + roomId + " not found", java.nio.charset.StandardCharsets.UTF_8) + 
                       (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
            }
            
            int successCount = 0;
            int errorCount = 0;
            StringBuilder errorMessages = new StringBuilder();
            
            // Process each selected date
            for (String dateStr : availableDates) {
                if (dateStr != null && !dateStr.trim().isEmpty()) {
                    try {
                        // Parse the date
                        java.time.LocalDate date = java.time.LocalDate.parse(dateStr);
                        
                        // Check if date is not in the past
                        if (date.isBefore(java.time.LocalDate.now())) {
                            errorCount++;
                            errorMessages.append("Date ").append(dateStr).append(": Cannot assign to past dates; ");
                            continue;
                        }
                        
                        // Assign doctor to room for this date
                        doctorScheduleService.assignDoctorToRoom(doctorId, roomId, periodName, date);
                        successCount++;
                    } catch (IllegalArgumentException e) {
                        errorCount++;
                        errorMessages.append("Date ").append(dateStr).append(": ").append(e.getMessage()).append("; ");
                    } catch (Exception e) {
                        errorCount++;
                        errorMessages.append("Date ").append(dateStr).append(": Assignment failed - ").append(e.getMessage()).append("; ");
                    }
                }
            }
            
            // Prepare response message
            if (successCount > 0 && errorCount == 0) {
                // All assignments successful
                return "redirect:/staff-coordinator/doctors/assignment?success=doctor_assigned_multiple&count=" + successCount + 
                       (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
            } else if (successCount > 0 && errorCount > 0) {
                // Partial success
                return "redirect:/staff-coordinator/doctors/assignment?warning=partial_assignment&success=" + successCount + "&errors=" + errorCount + 
                       (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
            } else {
                // All assignments failed
                return "redirect:/staff-coordinator/doctors/assignment?error=assignment_failed&details=" + 
                       java.net.URLEncoder.encode(errorMessages.toString(), java.nio.charset.StandardCharsets.UTF_8) + 
                       (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
            }
        } catch (Exception e) {
            // Other errors
            return "redirect:/staff-coordinator/doctors/assignment?error=assignment_failed" + 
                   (staffCoordinatorEmail != null ? "&email=" + staffCoordinatorEmail : "");
        }
    }
    
    // Appointment booking endpoint
    @PostMapping("/customer/appointments/book")
    public String bookAppointment(
            @RequestParam String patientName,
            @RequestParam String patientEmail,
            @RequestParam Long doctorId,
            @RequestParam String appointmentDate,
            @RequestParam String timeSlot,
            @RequestParam Long roomId,
            @RequestParam Long scheduleId,
            Model model) {
        
        // Basic validation
        if (patientName == null || patientName.trim().isEmpty()) {
            return "redirect:/appointment.html?error=patient_name_required";
        }
        
        if (patientEmail == null || patientEmail.trim().isEmpty() || !patientEmail.contains("@")) {
            return "redirect:/appointment.html?error=valid_email_required";
        }
        
        if (doctorId == null) {
            return "redirect:/appointment.html?error=doctor_required";
        }
        
        if (appointmentDate == null || appointmentDate.trim().isEmpty()) {
            return "redirect:/appointment.html?error=date_required";
        }
        
        if (timeSlot == null || timeSlot.trim().isEmpty()) {
            return "redirect:/appointment.html?error=time_slot_required";
        }
        
        if (roomId == null) {
            return "redirect:/appointment.html?error=room_required";
        }
        
        if (scheduleId == null) {
            return "redirect:/appointment.html?error=schedule_required";
        }
        
        try {
            // Parse the appointment date
            java.time.LocalDate date = java.time.LocalDate.parse(appointmentDate);
            
            // Book the appointment using AppointmentService
            Appointment appointment = appointmentService.bookAppointment(
                patientName.trim(),
                patientEmail.trim(),
                date,
                timeSlot.trim(),
                doctorId,
                roomId,
                scheduleId
            );
            
            // Redirect with success message
            return "redirect:/appointment.html?success=appointment_booked&appointmentId=" + appointment.getId();
            
        } catch (java.time.format.DateTimeParseException e) {
            return "redirect:/appointment.html?error=invalid_date_format";
        } catch (IllegalArgumentException e) {
            return "redirect:/appointment.html?error=booking_failed&details=" + 
                   java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Error booking appointment: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/appointment.html?error=booking_failed";
        }
    }
    
    // Test endpoint to verify appointment booking
    @GetMapping("/test-appointment")
    public String testAppointment(Model model) {
        try {
            // Test booking an appointment
            java.time.LocalDate testDate = java.time.LocalDate.now().plusDays(2);
            Appointment testAppointment = appointmentService.bookAppointment(
                "Test Patient",
                "test@example.com",
                testDate,
                "Afternoon",
                16L, // Use a valid doctor ID
                21L, // Use a valid room ID
                15L  // Use a different schedule ID
            );
            
            model.addAttribute("success", "Test appointment created successfully!");
            model.addAttribute("appointmentId", testAppointment.getId());
            return "test-result";
        } catch (Exception e) {
            model.addAttribute("error", "Test appointment failed: " + e.getMessage());
            return "test-result";
        }
    }
    
    // Feedback submission endpoint
    @PostMapping("/customer/feedback/submit")
    public String submitFeedback(
            @RequestParam String feedbackType,
            @RequestParam String message,
            @RequestParam Integer rating,
            @RequestParam String userEmail,
            Model model) {
        
        // Basic validation
        if (feedbackType == null || feedbackType.trim().isEmpty()) {
            return "redirect:/feedback.html?error=feedback_type_required";
        }
        
        if (message == null || message.trim().isEmpty()) {
            return "redirect:/feedback.html?error=message_required";
        }
        
        if (rating == null || rating < 1 || rating > 5) {
            return "redirect:/feedback.html?error=rating_required";
        }
        
        if (userEmail == null || userEmail.trim().isEmpty() || !userEmail.contains("@")) {
            return "redirect:/feedback.html?error=valid_email_required";
        }
        
        try {
            // Save feedback using FeedbackService
            Feedback feedback = feedbackService.saveFeedback(
                feedbackType.trim(),
                message.trim(),
                rating,
                userEmail.trim()
            );
            
            // Redirect with success message
            return "redirect:/feedback.html?success=feedback_submitted&feedbackId=" + feedback.getFeedbackId();
            
        } catch (IllegalArgumentException e) {
            return "redirect:/feedback.html?error=validation_failed&details=" + 
                   java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Error submitting feedback: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/feedback.html?error=submission_failed";
        }
    }

    // Support ticket creation endpoint
    @PostMapping("/customer/support/create")
    public String createSupportTicket(
            @RequestParam String category,
            @RequestParam String priority,
            @RequestParam String subject,
            @RequestParam String description,
            @RequestParam String userEmail,
            Model model) {
        
        // Basic validation
        if (category == null || category.trim().isEmpty()) {
            return "redirect:/support-ticket.html?error=category_required";
        }
        
        if (priority == null || priority.trim().isEmpty()) {
            return "redirect:/support-ticket.html?error=priority_required";
        }
        
        if (subject == null || subject.trim().isEmpty()) {
            return "redirect:/support-ticket.html?error=subject_required";
        }
        
        if (description == null || description.trim().isEmpty()) {
            return "redirect:/support-ticket.html?error=description_required";
        }
        
        if (userEmail == null || userEmail.trim().isEmpty() || !userEmail.contains("@")) {
            return "redirect:/support-ticket.html?error=valid_email_required";
        }
        
        try {
            // Create support ticket using SupportTicketService
            com.example.healthcare_customercare.entity.SupportTicket ticket = supportTicketService.createTicket(
                category.trim(),
                priority.trim(),
                subject.trim(),
                description.trim(),
                userEmail.trim()
            );
            
            // Redirect with success message
            return "redirect:/support-ticket.html?success=ticket_created&ticketId=" + ticket.getTicketId();
            
        } catch (IllegalArgumentException e) {
            return "redirect:/support-ticket.html?error=validation_failed&details=" + 
                   java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Error creating support ticket: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/support-ticket.html?error=creation_failed";
        }
    }
    
    // Test endpoint to verify support ticket creation
    @GetMapping("/test-support-ticket")
    public String testSupportTicket(Model model) {
        try {
            // Test creating a support ticket
            com.example.healthcare_customercare.entity.SupportTicket testTicket = supportTicketService.createTicket(
                "technical",
                "high",
                "Test Support Ticket",
                "This is a test support ticket to verify the system is working correctly.",
                "test@example.com"
            );
            
            model.addAttribute("success", "Test support ticket created successfully!");
            model.addAttribute("ticketId", testTicket.getTicketId());
            model.addAttribute("ticket", testTicket);
            return "test-result";
        } catch (Exception e) {
            model.addAttribute("error", "Test support ticket failed: " + e.getMessage());
            e.printStackTrace();
            return "test-result";
        }
    }
    
    // New Appointment Booking Endpoints
    @GetMapping("/receptionist/new-appointment")
    public String newAppointment(@RequestParam(required = false) String email, 
                                @RequestParam(required = false) String patientName,
                                @RequestParam(required = false) String patientEmail,
                                Model model, HttpSession session) {
        User receptionist = null;

        // First, try to get receptionist from session
        receptionist = (User) session.getAttribute("loggedInUser");

        // If not in session, try to get from URL parameter
        if (receptionist == null && email != null && !email.isEmpty()) {
            Optional<User> userOpt = userService.findUserByEmail(email);
            if (userOpt.isPresent() && "RECEPTIONIST".equals(userOpt.get().getRole())) {
                receptionist = userOpt.get();
                // Store in session for future requests
                session.setAttribute("loggedInUser", receptionist);
            }
        }

        // If still no receptionist found, create a mock receptionist
        if (receptionist == null) {
            receptionist = new User();
            receptionist.setFirstName("");
            receptionist.setLastName("");
            receptionist.setEmail("");
            receptionist.setPhoneNumber("");
            receptionist.setRole("RECEPTIONIST");
        }

        model.addAttribute("receptionist", receptionist);
        
        // Add patient information if provided
        if (patientName != null && !patientName.isEmpty()) {
            model.addAttribute("prefilledPatientName", patientName);
        }
        if (patientEmail != null && !patientEmail.isEmpty()) {
            model.addAttribute("prefilledPatientEmail", patientEmail);
        }
        
        return "Receptionist/new-appointment";
    }

    @PostMapping("/receptionist/appointment/create")
    public String createAppointment(
            @RequestParam String patientName,
            @RequestParam String patientEmail,
            @RequestParam String appointmentDate,
            @RequestParam Long docId,
            @RequestParam String timeSlot,
            @RequestParam Long roomId,
            @RequestParam Long scheduleId,
            @RequestParam String receptionistEmail,
            Model model) {

        System.out.println("=== CREATE APPOINTMENT DEBUG ===");
        System.out.println("Patient Name: " + patientName);
        System.out.println("Patient Email: " + patientEmail);
        System.out.println("Appointment Date: " + appointmentDate);
        System.out.println("Doctor ID: " + docId);
        System.out.println("Time Slot: " + timeSlot);
        System.out.println("Room ID: " + roomId);
        System.out.println("Schedule ID: " + scheduleId);
        System.out.println("Receptionist Email: " + receptionistEmail);

        try {
            // Parse the appointment date
            java.time.LocalDate date = java.time.LocalDate.parse(appointmentDate);
            System.out.println("Parsed date: " + date);

            // Create the appointment
            Appointment newAppointment = appointmentService.createAppointment(
                patientName.trim(), 
                patientEmail.trim(),
                date, 
                docId, 
                roomId, 
                scheduleId
            );
            System.out.println("Created appointment: " + newAppointment);

            // Redirect back to receptionist dashboard with success message
            String redirectUrl = "redirect:/receptionist.html?email=" + receptionistEmail + "&success=appointment_created";
            System.out.println("Redirect URL: " + redirectUrl);
            return redirectUrl;

        } catch (java.time.format.DateTimeParseException e) {
            System.err.println("Date parse error: " + e.getMessage());
            return "redirect:/receptionist/new-appointment?email=" + receptionistEmail + "&error=invalid_date";
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal argument error: " + e.getMessage());
            return "redirect:/receptionist/new-appointment?email=" + receptionistEmail + "&error=creation_failed&details=" +
                   java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("Error creating appointment: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/receptionist/new-appointment?email=" + receptionistEmail + "&error=creation_failed";
        }
    }
    
    // Test endpoint to create a receptionist user
    @GetMapping("/test-update-user-role")
    public String testUpdateUserRole(@RequestParam String email, @RequestParam String newRole, Model model) {
        try {
            Optional<User> userOpt = userService.findUserByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                String oldRole = user.getRole();
                user.setRole(newRole);
                User updatedUser = userService.saveUser(user);
                
                model.addAttribute("success", "User role updated successfully!");
                model.addAttribute("user", updatedUser);
                model.addAttribute("loginInfo", "User " + email + " role changed from " + oldRole + " to " + newRole);
            } else {
                model.addAttribute("error", "User not found with email: " + email);
            }
            return "test-result";
        } catch (Exception e) {
            model.addAttribute("error", "Error updating user role: " + e.getMessage());
            e.printStackTrace();
            return "test-result";
        }
    }


    @GetMapping("/test-create-receptionist")
    public String testCreateReceptionist(Model model) {
        try {
            // Test creating a receptionist user
            User testReceptionist = userService.createReceptionist(
                "Emma",
                "Wilson",
                "receptionist@arogya.com",
                "+94-77-123-4567",
                "receptionist123"
            );
            
            model.addAttribute("success", "Test receptionist created successfully!");
            model.addAttribute("receptionist", testReceptionist);
            model.addAttribute("loginInfo", "You can now login with email: receptionist@arogya.com and password: receptionist123");
            return "test-result";
        } catch (Exception e) {
            model.addAttribute("error", "Test receptionist creation failed: " + e.getMessage());
            e.printStackTrace();
            return "test-result";
        }
    }
    
    @GetMapping("/test-create-smo")
    public String testCreateSMO(Model model) {
        try {
            // Test creating a senior medical officer user
            User testSMO = userService.createSeniorMedicalOfficer(
                "Dr. Sarah",
                "Johnson",
                "smo@arogya.com",
                "+94-77-987-6543",
                "smo123"
            );
            
            model.addAttribute("success", "Test Senior Medical Officer created successfully!");
            model.addAttribute("smo", testSMO);
            model.addAttribute("loginInfo", "You can now login with email: smo@arogya.com and password: smo123");
            return "test-result";
        } catch (Exception e) {
            model.addAttribute("error", "Test SMO creation failed: " + e.getMessage());
            e.printStackTrace();
            return "test-result";
        }
    }
    
    // Test endpoint to test appointment update/delete functionality
    @GetMapping("/test-appointment-operations")
    public String testAppointmentOperations(Model model) {
        try {
            // Get all appointments
            List<Appointment> allAppointments = appointmentService.getAllAppointments();
            
            model.addAttribute("success", "Appointment operations test completed!");
            model.addAttribute("appointments", allAppointments);
            model.addAttribute("totalAppointments", allAppointments.size());
            
            return "test-result";
        } catch (Exception e) {
            model.addAttribute("error", "Appointment operations test failed: " + e.getMessage());
            e.printStackTrace();
            return "test-result";
        }
    }
    
    // Test endpoint to create a sample patient with appointments
    @GetMapping("/test-create-patient-with-appointments")
    public String testCreatePatientWithAppointments(Model model) {
        try {
            // Create a test patient
            User testPatient = userService.createCustomer(
                "John",
                "Doe",
                "john.doe@example.com",
                "+94-77-987-6543",
                "patient123"
            );
            
            // Create some test appointments for this patient
            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.LocalDate tomorrow = today.plusDays(1);
            java.time.LocalDate nextWeek = today.plusDays(7);
            
            List<Appointment> testAppointments = new java.util.ArrayList<>();
            
            try {
                // Try to create appointments (this might fail if no schedules exist, but that's okay)
                Appointment appointment1 = appointmentService.bookAppointment(
                    "John Doe",
                    "john.doe@example.com",
                    tomorrow,
                    "Morning",
                    16L, // Use a valid doctor ID
                    21L, // Use a valid room ID
                    15L  // Use a valid schedule ID
                );
                testAppointments.add(appointment1);
            } catch (Exception e) {
                System.out.println("Could not create appointment 1: " + e.getMessage());
            }
            
            try {
                Appointment appointment2 = appointmentService.bookAppointment(
                    "John Doe",
                    "john.doe@example.com",
                    nextWeek,
                    "Afternoon",
                    17L, // Use a different doctor ID
                    22L, // Use a different room ID
                    16L  // Use a different schedule ID
                );
                testAppointments.add(appointment2);
            } catch (Exception e) {
                System.out.println("Could not create appointment 2: " + e.getMessage());
            }
            
            model.addAttribute("success", "Test patient created successfully!");
            model.addAttribute("patient", testPatient);
            model.addAttribute("appointments", testAppointments);
            model.addAttribute("loginInfo", "You can now lookup this patient with email: john.doe@example.com");
            model.addAttribute("testInstructions", "To test patient lookup: 1) Login as receptionist, 2) Go to Patient Lookup, 3) Search for john.doe@example.com");
            
            return "test-result";
        } catch (Exception e) {
            model.addAttribute("error", "Test patient creation failed: " + e.getMessage());
            e.printStackTrace();
            return "test-result";
        }
    }
    
    // Endpoint to view all support tickets
    @GetMapping("/view-support-tickets")
    public String viewSupportTickets(Model model) {
        try {
            java.util.List<com.example.healthcare_customercare.entity.SupportTicket> tickets = supportTicketService.getAllTickets();
            model.addAttribute("tickets", tickets);
            model.addAttribute("ticketCount", tickets.size());
            return "test-result";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to retrieve support tickets: " + e.getMessage());
            e.printStackTrace();
            return "test-result";
        }
    }
    
    // Profile Settings Endpoints
    @GetMapping("/profile-settings.html")
    public String profileSettings(@RequestParam(required = false) String email, Model model, HttpSession session) {
        User customer = null;
        
        // First, try to get customer from session
        customer = (User) session.getAttribute("loggedInUser");
        
        // If not in session, try to get from URL parameter
        if (customer == null && email != null && !email.isEmpty()) {
            Optional<User> userOpt = userService.findUserByEmail(email);
            if (userOpt.isPresent()) {
                customer = userOpt.get();
                // Store in session for future requests
                session.setAttribute("loggedInUser", customer);
            }
        }
        
        // If still no customer found, create a mock customer
        if (customer == null) {
            customer = new User();
            customer.setFirstName("");
            customer.setLastName("");
            customer.setEmail("");
            customer.setPhoneNumber("");
        }
        
        model.addAttribute("customer", customer);
        return "Customer/profile-settings";
    }
    
    // Staff Coordinator Profile Settings Endpoints
    @GetMapping("/staff-coordinator/profile-settings")
    public String staffCoordinatorProfileSettings(@RequestParam(required = false) String email, Model model, HttpSession session) {
        User staffCoordinator = null;
        
        // First, try to get staff coordinator from session
        staffCoordinator = (User) session.getAttribute("loggedInUser");
        
        // If not in session, try to get from URL parameter
        if (staffCoordinator == null && email != null && !email.isEmpty()) {
            Optional<User> userOpt = userService.findUserByEmail(email);
            if (userOpt.isPresent() && "STAFF_COORDINATOR".equals(userOpt.get().getRole())) {
                staffCoordinator = userOpt.get();
                // Store in session for future requests
                session.setAttribute("loggedInUser", staffCoordinator);
            }
        }
        
        // If still no staff coordinator found, create a mock staff coordinator
        if (staffCoordinator == null) {
            staffCoordinator = new User();
            staffCoordinator.setFirstName("");
            staffCoordinator.setLastName("");
            staffCoordinator.setEmail("");
            staffCoordinator.setPhoneNumber("");
            staffCoordinator.setRole("STAFF_COORDINATOR");
        }
        
        model.addAttribute("staffCoordinator", staffCoordinator);
        return "Staff-Coordinator/profile-settings";
    }
    
    // Receptionist Profile Settings Endpoints
    @GetMapping("/receptionist/profile-settings")
    public String receptionistProfileSettings(@RequestParam(required = false) String email, Model model, HttpSession session) {
        User receptionist = null;
        
        // First, try to get receptionist from session
        receptionist = (User) session.getAttribute("loggedInUser");
        
        // If not in session, try to get from URL parameter
        if (receptionist == null && email != null && !email.isEmpty()) {
            Optional<User> userOpt = userService.findUserByEmail(email);
            if (userOpt.isPresent() && "RECEPTIONIST".equals(userOpt.get().getRole())) {
                receptionist = userOpt.get();
                // Store in session for future requests
                session.setAttribute("loggedInUser", receptionist);
            }
        }
        
        // If still no receptionist found, create a mock receptionist
        if (receptionist == null) {
            receptionist = new User();
            receptionist.setFirstName("");
            receptionist.setLastName("");
            receptionist.setEmail("");
            receptionist.setPhoneNumber("");
            receptionist.setRole("RECEPTIONIST");
        }
        
        model.addAttribute("receptionist", receptionist);
        return "Receptionist/profile-settings";
    }
    
    // Patient Lookup Endpoints
    @GetMapping("/receptionist/patient-lookup")
    public String patientLookup(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String patientEmail,
            @RequestParam(required = false) String receptionistEmail,
            Model model, 
            HttpSession session) {
        
        User receptionist = null;
        
        // First, try to get receptionist from session
        receptionist = (User) session.getAttribute("loggedInUser");
        
        // If not in session, try to get from URL parameter
        if (receptionist == null && receptionistEmail != null && !receptionistEmail.isEmpty()) {
            Optional<User> userOpt = userService.findUserByEmail(receptionistEmail);
            if (userOpt.isPresent() && "RECEPTIONIST".equals(userOpt.get().getRole())) {
                receptionist = userOpt.get();
                // Store in session for future requests
                session.setAttribute("loggedInUser", receptionist);
            }
        }
        
        // Fallback to email parameter for backward compatibility
        if (receptionist == null && email != null && !email.isEmpty()) {
            Optional<User> userOpt = userService.findUserByEmail(email);
            if (userOpt.isPresent() && "RECEPTIONIST".equals(userOpt.get().getRole())) {
                receptionist = userOpt.get();
                session.setAttribute("loggedInUser", receptionist);
            }
        }
        
        // If still no receptionist found, create a mock receptionist
        if (receptionist == null) {
            receptionist = new User();
            receptionist.setFirstName("");
            receptionist.setLastName("");
            receptionist.setEmail("");
            receptionist.setPhoneNumber("");
            receptionist.setRole("RECEPTIONIST");
        }
        
        // If we have a patientEmail parameter, perform the search
        if (patientEmail != null && !patientEmail.trim().isEmpty()) {
            try {
                // Find the patient by email
                Optional<User> patientOpt = userService.findUserByEmail(patientEmail.trim());
                
                if (patientOpt.isPresent()) {
                    User patient = patientOpt.get();
                    
                    // Get patient's appointments
                    List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientEmail.trim());
                    
                    // Enrich appointments with doctor information
                    List<Map<String, Object>> enrichedAppointments = new java.util.ArrayList<>();
                    for (Appointment appointment : appointments) {
                        Map<String, Object> appointmentData = new HashMap<>();
                        appointmentData.put("id", appointment.getId());
                        appointmentData.put("patientName", appointment.getPatientName());
                        appointmentData.put("appointmentDate", appointment.getAppointmentDate());
                        appointmentData.put("docId", appointment.getDocId());
                        appointmentData.put("roomId", appointment.getRoomId());
                        appointmentData.put("scheduleId", appointment.getScheduleId());
                        
                        // Get doctor information
                        try {
                            Optional<com.example.healthcare_customercare.entity.Doctor> doctorOpt = doctorService.getDoctorById(appointment.getDocId());
                            if (doctorOpt.isPresent()) {
                                com.example.healthcare_customercare.entity.Doctor doctor = doctorOpt.get();
                                appointmentData.put("doctorName", doctor.getDoctorName());
                                appointmentData.put("specialization", doctor.getSpecialization());
                                appointmentData.put("department", doctor.getDepartment());
                            } else {
                                appointmentData.put("doctorName", "Unknown Doctor");
                                appointmentData.put("specialization", "Unknown");
                                appointmentData.put("department", "Unknown");
                            }
                        } catch (Exception e) {
                            appointmentData.put("doctorName", "Unknown Doctor");
                            appointmentData.put("specialization", "Unknown");
                            appointmentData.put("department", "Unknown");
                        }
                        
                        enrichedAppointments.add(appointmentData);
                    }
                    
                    // Add data to model
                    model.addAttribute("patient", patient);
                    model.addAttribute("appointments", enrichedAppointments);
                }
                
            } catch (Exception e) {
                System.err.println("Error loading patient data: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        model.addAttribute("receptionist", receptionist);
        return "Receptionist/patient-lookup";
    }
    
    @PostMapping("/receptionist/patient-lookup/search")
    public String searchPatient(
            @RequestParam String patientEmail,
            @RequestParam String receptionistEmail,
            Model model,
            HttpSession session) {
        
        // Basic validation
        if (patientEmail == null || patientEmail.trim().isEmpty()) {
            return "redirect:/receptionist/patient-lookup?error=email_required&email=" + receptionistEmail;
        }
        
        if (!patientEmail.contains("@")) {
            return "redirect:/receptionist/patient-lookup?error=invalid_email&email=" + receptionistEmail;
        }
        
        try {
            // Find the patient by email
            Optional<User> patientOpt = userService.findUserByEmail(patientEmail.trim());
            
            if (patientOpt.isEmpty()) {
                return "redirect:/receptionist/patient-lookup?error=patient_not_found&email=" + receptionistEmail;
            }
            
            User patient = patientOpt.get();
            
            // Get receptionist info
            User receptionist = null;
            receptionist = (User) session.getAttribute("loggedInUser");
            
            if (receptionist == null && receptionistEmail != null && !receptionistEmail.isEmpty()) {
                Optional<User> userOpt = userService.findUserByEmail(receptionistEmail);
                if (userOpt.isPresent() && "RECEPTIONIST".equals(userOpt.get().getRole())) {
                    receptionist = userOpt.get();
                    session.setAttribute("loggedInUser", receptionist);
                }
            }
            
            if (receptionist == null) {
                receptionist = new User();
                receptionist.setFirstName("");
                receptionist.setLastName("");
                receptionist.setEmail("");
                receptionist.setPhoneNumber("");
                receptionist.setRole("RECEPTIONIST");
            }
            
            // Get patient's appointments
            List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientEmail.trim());
            
            // Enrich appointments with doctor information
            List<Map<String, Object>> enrichedAppointments = new java.util.ArrayList<>();
            for (Appointment appointment : appointments) {
                Map<String, Object> appointmentData = new HashMap<>();
                appointmentData.put("id", appointment.getId());
                appointmentData.put("patientName", appointment.getPatientName());
                appointmentData.put("appointmentDate", appointment.getAppointmentDate());
                appointmentData.put("docId", appointment.getDocId());
                appointmentData.put("roomId", appointment.getRoomId());
                appointmentData.put("scheduleId", appointment.getScheduleId());
                
                // Get doctor information
                try {
                    Optional<com.example.healthcare_customercare.entity.Doctor> doctorOpt = doctorService.getDoctorById(appointment.getDocId());
                    if (doctorOpt.isPresent()) {
                        com.example.healthcare_customercare.entity.Doctor doctor = doctorOpt.get();
                        appointmentData.put("doctorName", doctor.getDoctorName());
                        appointmentData.put("specialization", doctor.getSpecialization());
                        appointmentData.put("department", doctor.getDepartment());
                    } else {
                        appointmentData.put("doctorName", "Unknown Doctor");
                        appointmentData.put("specialization", "Unknown");
                        appointmentData.put("department", "Unknown");
                    }
                } catch (Exception e) {
                    appointmentData.put("doctorName", "Unknown Doctor");
                    appointmentData.put("specialization", "Unknown");
                    appointmentData.put("department", "Unknown");
                }
                
                enrichedAppointments.add(appointmentData);
            }
            
            // Add data to model
            model.addAttribute("receptionist", receptionist);
            model.addAttribute("patient", patient);
            model.addAttribute("appointments", enrichedAppointments);
            
            return "Receptionist/patient-lookup";
            
        } catch (Exception e) {
            System.err.println("Error searching for patient: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/receptionist/patient-lookup?error=search_failed&email=" + receptionistEmail;
        }
    }
    
    // Appointment Edit/Delete Endpoints
    @GetMapping("/receptionist/appointment/edit")
    public String editAppointmentForm(
            @RequestParam Long appointmentId,
            @RequestParam String patientEmail,
            @RequestParam String receptionistEmail,
            Model model) {
        
        try {
            // Get appointment details
            Optional<Appointment> appointmentOpt = appointmentService.getAppointmentById(appointmentId);
            if (appointmentOpt.isEmpty()) {
                return "redirect:/receptionist/patient-lookup?error=appointment_not_found&email=" + receptionistEmail;
            }
            
            Appointment appointment = appointmentOpt.get();
            
            // Get receptionist info
            User receptionist = null;
            Optional<User> userOpt = userService.findUserByEmail(receptionistEmail);
            if (userOpt.isPresent() && "RECEPTIONIST".equals(userOpt.get().getRole())) {
                receptionist = userOpt.get();
            }
            
            if (receptionist == null) {
                receptionist = new User();
                receptionist.setFirstName("");
                receptionist.setLastName("");
                receptionist.setEmail("");
                receptionist.setPhoneNumber("");
                receptionist.setRole("RECEPTIONIST");
            }
            
            // Get patient info
            Optional<User> patientOpt = userService.findUserByEmail(patientEmail);
            User patient = patientOpt.orElse(new User());
            
            // Get available doctors and rooms for the form
            model.addAttribute("receptionist", receptionist);
            model.addAttribute("patient", patient);
            model.addAttribute("appointment", appointment);
            model.addAttribute("doctors", doctorService.getAllDoctors());
            model.addAttribute("rooms", roomService.getAvailableRooms());
            
            return "Receptionist/edit-appointment";
            
        } catch (Exception e) {
            System.err.println("Error loading appointment edit form: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/receptionist/patient-lookup?error=load_failed&email=" + receptionistEmail;
        }
    }
    
    @PostMapping("/receptionist/appointment/update")
    public String updateAppointment(
            @RequestParam Long appointmentId,
            @RequestParam String patientName,
            @RequestParam String patientEmail,
            @RequestParam String appointmentDate,
            @RequestParam Long docId,
            @RequestParam(required = false) String timeSlot,
            @RequestParam(required = false) Long roomId,
            @RequestParam(required = false) Long scheduleId,
            @RequestParam String receptionistEmail,
            Model model) {
        
        System.out.println("=== UPDATE APPOINTMENT DEBUG ===");
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Patient Name: " + patientName);
        System.out.println("Patient Email: " + patientEmail);
        System.out.println("Appointment Date: " + appointmentDate);
        System.out.println("Doctor ID: " + docId);
        System.out.println("Time Slot: " + timeSlot);
        System.out.println("Room ID: " + roomId);
        System.out.println("Schedule ID: " + scheduleId);
        System.out.println("Receptionist Email: " + receptionistEmail);
        
        try {
            // Parse the appointment date
            java.time.LocalDate date = java.time.LocalDate.parse(appointmentDate);
            System.out.println("Parsed date: " + date);
            
            // If timeSlot, roomId, and scheduleId are provided (from new form), use them
            // Otherwise, get the current appointment values
            Long finalRoomId = roomId;
            Long finalScheduleId = scheduleId;
            
            if (timeSlot != null && roomId != null && scheduleId != null) {
                // New time slot selected - use the provided values
                System.out.println("Using new time slot selection");
            } else {
                // Fallback to current appointment values
                Optional<Appointment> currentAppointmentOpt = appointmentService.getAppointmentById(appointmentId);
                if (currentAppointmentOpt.isPresent()) {
                    Appointment currentAppointment = currentAppointmentOpt.get();
                    finalRoomId = currentAppointment.getRoomId();
                    finalScheduleId = currentAppointment.getScheduleId();
                    System.out.println("Using current appointment values - Room: " + finalRoomId + ", Schedule: " + finalScheduleId);
                } else {
                    throw new IllegalArgumentException("Appointment not found");
                }
            }
            
            // Update the appointment
            Appointment updatedAppointment = appointmentService.updateAppointment(appointmentId, patientName.trim(), patientEmail.trim(),
                    date, docId, finalRoomId, finalScheduleId);
            System.out.println("Updated appointment: " + updatedAppointment);
            
            // Redirect back to patient lookup with success message
            String redirectUrl = "redirect:/receptionist/patient-lookup?patientEmail=" + patientEmail + 
                   "&receptionistEmail=" + receptionistEmail + "&success=appointment_updated";
            System.out.println("Redirect URL: " + redirectUrl);
            return redirectUrl;
            
        } catch (java.time.format.DateTimeParseException e) {
            System.err.println("Date parse error: " + e.getMessage());
            return "redirect:/receptionist/patient-lookup?error=invalid_date&email=" + receptionistEmail;
        } catch (IllegalArgumentException e) {
            System.err.println("Illegal argument error: " + e.getMessage());
            return "redirect:/receptionist/patient-lookup?error=update_failed&details=" + 
                   java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8) +
                   "&email=" + receptionistEmail;
        } catch (Exception e) {
            System.err.println("Error updating appointment: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/receptionist/patient-lookup?error=update_failed&email=" + receptionistEmail;
        }
    }
    
    @PostMapping("/receptionist/appointment/delete")
    public String deleteAppointment(
            @RequestParam Long appointmentId,
            @RequestParam String patientEmail,
            @RequestParam String receptionistEmail,
            Model model) {
        
        System.out.println("=== DELETE APPOINTMENT DEBUG ===");
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Patient Email: " + patientEmail);
        System.out.println("Receptionist Email: " + receptionistEmail);
        
        try {
            // Delete the appointment
            boolean deleted = appointmentService.deleteAppointment(appointmentId);
            System.out.println("Delete result: " + deleted);
            
            if (deleted) {
                // Redirect back to patient lookup with success message
                String redirectUrl = "redirect:/receptionist/patient-lookup?patientEmail=" + patientEmail + 
                       "&receptionistEmail=" + receptionistEmail + "&success=appointment_deleted";
                System.out.println("Redirect URL: " + redirectUrl);
                return redirectUrl;
            } else {
                System.out.println("Appointment not found for deletion");
                return "redirect:/receptionist/patient-lookup?error=appointment_not_found&email=" + receptionistEmail;
            }
            
        } catch (Exception e) {
            System.err.println("Error deleting appointment: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/receptionist/patient-lookup?error=delete_failed&email=" + receptionistEmail;
        }
    }
    
    @PostMapping("/receptionist/profile-settings/update")
    public String updateReceptionistProfile(
            @RequestParam String email,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phoneNumber,
            Model model,
            HttpSession session) {
        
        // Basic validation
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return "redirect:/receptionist/profile-settings?error=valid_email_required&email=" + email;
        }
        
        if (firstName == null || firstName.trim().isEmpty()) {
            return "redirect:/receptionist/profile-settings?error=first_name_required&email=" + email;
        }
        
        if (lastName == null || lastName.trim().isEmpty()) {
            return "redirect:/receptionist/profile-settings?error=last_name_required&email=" + email;
        }
        
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "redirect:/receptionist/profile-settings?error=phone_number_required&email=" + email;
        }
        
        try {
            // Update user profile
            userService.updateUserProfile(email.trim(), firstName.trim(), lastName.trim(), phoneNumber.trim());
            
            // Update the session with the new user data
            Optional<User> updatedUser = userService.findUserByEmail(email.trim());
            if (updatedUser.isPresent()) {
                session.setAttribute("loggedInUser", updatedUser.get());
            }
            
            // Redirect with success message
            return "redirect:/receptionist/profile-settings?success=profile_updated&email=" + email;
            
        } catch (IllegalArgumentException e) {
            return "redirect:/receptionist/profile-settings?error=user_not_found&email=" + email;
        } catch (Exception e) {
            System.err.println("Error updating receptionist profile: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/receptionist/profile-settings?error=update_failed&email=" + email;
        }
    }
    
    @PostMapping("/receptionist/profile-settings/change-password")
    public String changeReceptionistPassword(
            @RequestParam String email,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model,
            HttpSession session) {
        
        // Basic validation
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return "redirect:/receptionist/profile-settings?error=valid_email_required&email=" + email;
        }
        
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            return "redirect:/receptionist/profile-settings?error=current_password_required&email=" + email;
        }
        
        if (newPassword == null || newPassword.length() < 8) {
            return "redirect:/receptionist/profile-settings?error=new_password_min_length&email=" + email;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/receptionist/profile-settings?error=passwords_do_not_match&email=" + email;
        }
        
        try {
            // Update user password
            userService.updateUserPassword(email.trim(), currentPassword, newPassword);
            
            // Redirect with success message
            return "redirect:/receptionist/profile-settings?success=password_updated&email=" + email;
            
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return "redirect:/receptionist/profile-settings?error=user_not_found&email=" + email;
            } else {
                return "redirect:/receptionist/profile-settings?error=current_password_incorrect&email=" + email;
            }
        } catch (Exception e) {
            System.err.println("Error changing receptionist password: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/receptionist/profile-settings?error=password_change_failed&email=" + email;
        }
    }
    
    @PostMapping("/staff-coordinator/profile-settings/update")
    public String updateStaffCoordinatorProfile(
            @RequestParam String email,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phoneNumber,
            Model model,
            HttpSession session) {
        
        // Basic validation
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return "redirect:/staff-coordinator/profile-settings?error=valid_email_required&email=" + email;
        }
        
        if (firstName == null || firstName.trim().isEmpty()) {
            return "redirect:/staff-coordinator/profile-settings?error=first_name_required&email=" + email;
        }
        
        if (lastName == null || lastName.trim().isEmpty()) {
            return "redirect:/staff-coordinator/profile-settings?error=last_name_required&email=" + email;
        }
        
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "redirect:/staff-coordinator/profile-settings?error=phone_number_required&email=" + email;
        }
        
        try {
            // Update user profile
            userService.updateUserProfile(email.trim(), firstName.trim(), lastName.trim(), phoneNumber.trim());
            
            // Update the session with the new user data
            Optional<User> updatedUser = userService.findUserByEmail(email.trim());
            if (updatedUser.isPresent()) {
                session.setAttribute("loggedInUser", updatedUser.get());
            }
            
            // Redirect with success message
            return "redirect:/staff-coordinator/profile-settings?success=profile_updated&email=" + email;
            
        } catch (IllegalArgumentException e) {
            return "redirect:/staff-coordinator/profile-settings?error=user_not_found&email=" + email;
        } catch (Exception e) {
            System.err.println("Error updating staff coordinator profile: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/staff-coordinator/profile-settings?error=update_failed&email=" + email;
        }
    }
    
    @PostMapping("/staff-coordinator/profile-settings/change-password")
    public String changeStaffCoordinatorPassword(
            @RequestParam String email,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model,
            HttpSession session) {
        
        // Basic validation
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return "redirect:/staff-coordinator/profile-settings?error=valid_email_required&email=" + email;
        }
        
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            return "redirect:/staff-coordinator/profile-settings?error=current_password_required&email=" + email;
        }
        
        if (newPassword == null || newPassword.length() < 8) {
            return "redirect:/staff-coordinator/profile-settings?error=new_password_min_length&email=" + email;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/staff-coordinator/profile-settings?error=passwords_do_not_match&email=" + email;
        }
        
        try {
            // Update user password
            userService.updateUserPassword(email.trim(), currentPassword, newPassword);
            
            // Redirect with success message
            return "redirect:/staff-coordinator/profile-settings?success=password_updated&email=" + email;
            
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return "redirect:/staff-coordinator/profile-settings?error=user_not_found&email=" + email;
            } else {
                return "redirect:/staff-coordinator/profile-settings?error=current_password_incorrect&email=" + email;
            }
        } catch (Exception e) {
            System.err.println("Error changing staff coordinator password: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/staff-coordinator/profile-settings?error=password_change_failed&email=" + email;
        }
    }
    
    @PostMapping("/profile-settings/update")
    public String updateProfile(
            @RequestParam String email,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String phoneNumber,
            Model model,
            HttpSession session) {
        
        // Basic validation
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return "redirect:/profile-settings.html?error=valid_email_required&email=" + email;
        }
        
        if (firstName == null || firstName.trim().isEmpty()) {
            return "redirect:/profile-settings.html?error=first_name_required&email=" + email;
        }
        
        if (lastName == null || lastName.trim().isEmpty()) {
            return "redirect:/profile-settings.html?error=last_name_required&email=" + email;
        }
        
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return "redirect:/profile-settings.html?error=phone_number_required&email=" + email;
        }
        
        try {
            // Update user profile
            userService.updateUserProfile(email.trim(), firstName.trim(), lastName.trim(), phoneNumber.trim());
            
            // Update the session with the new user data
            Optional<User> updatedUser = userService.findUserByEmail(email.trim());
            if (updatedUser.isPresent()) {
                session.setAttribute("loggedInUser", updatedUser.get());
            }
            
            // Redirect with success message
            return "redirect:/profile-settings.html?success=profile_updated&email=" + email;
            
        } catch (IllegalArgumentException e) {
            return "redirect:/profile-settings.html?error=user_not_found&email=" + email;
        } catch (Exception e) {
            System.err.println("Error updating profile: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/profile-settings.html?error=update_failed&email=" + email;
        }
    }
    
    @PostMapping("/profile-settings/change-password")
    public String changePassword(
            @RequestParam String email,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {
        
        // Basic validation
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            return "redirect:/profile-settings.html?error=valid_email_required&email=" + email;
        }
        
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            return "redirect:/profile-settings.html?error=current_password_required&email=" + email;
        }
        
        if (newPassword == null || newPassword.length() < 8) {
            return "redirect:/profile-settings.html?error=new_password_min_length&email=" + email;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/profile-settings.html?error=passwords_do_not_match&email=" + email;
        }
        
        try {
            // Update user password
            userService.updateUserPassword(email.trim(), currentPassword, newPassword);
            
            // Redirect with success message
            return "redirect:/profile-settings.html?success=password_updated&email=" + email;
            
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("not found")) {
                return "redirect:/profile-settings.html?error=user_not_found&email=" + email;
            } else {
                return "redirect:/profile-settings.html?error=current_password_incorrect&email=" + email;
            }
        } catch (Exception e) {
            System.err.println("Error changing password: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/profile-settings.html?error=password_change_failed&email=" + email;
        }
    }
    
    // API endpoint to get appointments by date
    @GetMapping("/api/appointments/by-date")
    @ResponseBody
    public Map<String, Object> getAppointmentsByDate(@RequestParam String date) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("=== DEBUG: getAppointmentsByDate called ===");
            System.out.println("Date parameter: " + date);
            
            // Parse the date string
            java.time.LocalDate appointmentDate = java.time.LocalDate.parse(date);
            System.out.println("Parsed date: " + appointmentDate);
            
            // Get appointments for the specified date
            List<Appointment> appointments = appointmentService.getAppointmentsByDate(appointmentDate);
            System.out.println("Found " + appointments.size() + " appointments for date: " + appointmentDate);
            
            // Enrich appointments with doctor and schedule information
            List<Map<String, Object>> enrichedAppointments = new java.util.ArrayList<>();
            for (Appointment appointment : appointments) {
                Map<String, Object> appointmentData = new HashMap<>();
                appointmentData.put("id", appointment.getId());
                appointmentData.put("patientName", appointment.getPatientName());
                appointmentData.put("patientEmail", appointment.getPatientEmail());
                appointmentData.put("appointmentDate", appointment.getAppointmentDate());
                appointmentData.put("docId", appointment.getDocId());
                appointmentData.put("roomId", appointment.getRoomId());
                appointmentData.put("scheduleId", appointment.getScheduleId());
                
                // Get doctor information
                try {
                    Optional<com.example.healthcare_customercare.entity.Doctor> doctorOpt = doctorService.getDoctorById(appointment.getDocId());
                    if (doctorOpt.isPresent()) {
                        com.example.healthcare_customercare.entity.Doctor doctor = doctorOpt.get();
                        appointmentData.put("doctorName", doctor.getDoctorName());
                        appointmentData.put("specialization", doctor.getSpecialization());
                        appointmentData.put("department", doctor.getDepartment());
                        System.out.println("Found doctor: " + doctor.getDoctorName());
                    } else {
                        appointmentData.put("doctorName", "Unknown Doctor");
                        appointmentData.put("specialization", "Unknown");
                        appointmentData.put("department", "Unknown");
                        System.out.println("Doctor not found for ID: " + appointment.getDocId());
                    }
                } catch (Exception e) {
                    appointmentData.put("doctorName", "Unknown Doctor");
                    appointmentData.put("specialization", "Unknown");
                    appointmentData.put("department", "Unknown");
                    System.out.println("Error getting doctor info: " + e.getMessage());
                }
                
                // Get schedule information for time slot
                try {
                    Optional<com.example.healthcare_customercare.entity.DoctorSchedule> scheduleOpt = doctorScheduleService.getScheduleById(appointment.getScheduleId());
                    if (scheduleOpt.isPresent()) {
                        com.example.healthcare_customercare.entity.DoctorSchedule schedule = scheduleOpt.get();
                        appointmentData.put("timeSlot", schedule.getTimePeriod());
                        appointmentData.put("startTime", schedule.getStartTime());
                        appointmentData.put("endTime", schedule.getEndTime());
                        System.out.println("Found schedule time period: " + schedule.getTimePeriod());
                    } else {
                        appointmentData.put("timeSlot", "N/A");
                        appointmentData.put("startTime", "N/A");
                        appointmentData.put("endTime", "N/A");
                        System.out.println("Schedule not found for ID: " + appointment.getScheduleId());
                    }
                } catch (Exception e) {
                    appointmentData.put("timeSlot", "N/A");
                    appointmentData.put("startTime", "N/A");
                    appointmentData.put("endTime", "N/A");
                    System.out.println("Error getting schedule info: " + e.getMessage());
                }
                
                // Get room information
                try {
                    Optional<com.example.healthcare_customercare.entity.Room> roomOpt = roomService.getRoomById(appointment.getRoomId());
                    if (roomOpt.isPresent()) {
                        com.example.healthcare_customercare.entity.Room room = roomOpt.get();
                        appointmentData.put("roomNumber", "Room " + room.getRoomId());
                        appointmentData.put("roomType", "Consultation Room");
                        appointmentData.put("roomCapacity", room.getRoomCapacity());
                        System.out.println("Found room: Room " + room.getRoomId());
                    } else {
                        appointmentData.put("roomNumber", "Room " + appointment.getRoomId());
                        appointmentData.put("roomType", "Unknown");
                        appointmentData.put("roomCapacity", "Unknown");
                        System.out.println("Room not found for ID: " + appointment.getRoomId());
                    }
                } catch (Exception e) {
                    appointmentData.put("roomNumber", "Room " + appointment.getRoomId());
                    appointmentData.put("roomType", "Unknown");
                    appointmentData.put("roomCapacity", "Unknown");
                    System.out.println("Error getting room info: " + e.getMessage());
                }
                
                enrichedAppointments.add(appointmentData);
            }
            
            response.put("success", true);
            response.put("appointments", enrichedAppointments);
            response.put("count", enrichedAppointments.size());
            response.put("date", appointmentDate.toString());
            
            System.out.println("Successfully returned " + enrichedAppointments.size() + " appointments");
            
        } catch (Exception e) {
            System.err.println("Error getting appointments by date: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error retrieving appointments: " + e.getMessage());
            response.put("appointments", new java.util.ArrayList<>());
            response.put("count", 0);
        }
        
        return response;
    }
    
    // API endpoint to get room capacity status
    @GetMapping("/api/rooms/capacity-status")
    @ResponseBody
    public Map<String, Object> getRoomCapacityStatus() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            System.out.println("=== DEBUG: getRoomCapacityStatus called ===");
            
            // Get all rooms with capacity information
            List<com.example.healthcare_customercare.entity.Room> rooms = roomService.getRoomCapacityStatus();
            System.out.println("Found " + rooms.size() + " rooms");
            
            // Process room data
            List<Map<String, Object>> roomStatusList = new java.util.ArrayList<>();
            int availableRooms = 0;
            int fullRooms = 0;
            
            for (com.example.healthcare_customercare.entity.Room room : rooms) {
                Map<String, Object> roomData = new HashMap<>();
                roomData.put("roomId", room.getRoomId());
                roomData.put("roomNumber", "Room " + room.getRoomId());
                roomData.put("roomCapacity", room.getRoomCapacity());
                roomData.put("currentCapacity", room.getCurrentCapacity());
                roomData.put("remainingCapacity", room.getRemainingCapacity());
                roomData.put("capacityPercentage", room.getCapacityPercentage());
                roomData.put("isAvailable", room.isAvailable());
                
                // Determine status color
                String statusColor = "green";
                if (room.getCapacityPercentage() >= 100) {
                    statusColor = "red";
                    fullRooms++;
                } else if (room.getCapacityPercentage() >= 80) {
                    statusColor = "yellow";
                    availableRooms++;
                } else {
                    availableRooms++;
                }
                
                roomData.put("statusColor", statusColor);
                
                roomStatusList.add(roomData);
                System.out.println("Room " + room.getRoomId() + ": " + room.getCurrentCapacity() + "/" + room.getRoomCapacity());
            }
            
            response.put("success", true);
            response.put("rooms", roomStatusList);
            response.put("totalRooms", rooms.size());
            response.put("availableRooms", availableRooms);
            response.put("fullRooms", fullRooms);
            
            System.out.println("Successfully returned room capacity status");
            
        } catch (Exception e) {
            System.err.println("Error getting room capacity status: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error retrieving room capacity status: " + e.getMessage());
            response.put("rooms", new java.util.ArrayList<>());
            response.put("totalRooms", 0);
            response.put("availableRooms", 0);
            response.put("fullRooms", 0);
        }
        
        return response;
    }
    
    // PDF Export Endpoints
    
    /**
     * Export all doctors to PDF
     */
    @GetMapping("/staff-coordinator/doctors/export/pdf")
    public ResponseEntity<byte[]> exportAllDoctorsToPdf() {
        try {
            byte[] pdfBytes = pdfExportService.exportDoctorsToPdf();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "doctors_report_" + 
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error exporting doctors to PDF: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Export doctors by specialization to PDF
     */
    @GetMapping("/staff-coordinator/doctors/export/pdf/specialization/{specialization}")
    public ResponseEntity<byte[]> exportDoctorsBySpecializationToPdf(@PathVariable String specialization) {
        try {
            byte[] pdfBytes = pdfExportService.exportDoctorsBySpecializationToPdf(specialization);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "doctors_" + specialization.replaceAll("\\s+", "_") + "_report_" + 
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error exporting doctors by specialization to PDF: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Export doctors by department to PDF
     */
    @GetMapping("/staff-coordinator/doctors/export/pdf/department/{department}")
    public ResponseEntity<byte[]> exportDoctorsByDepartmentToPdf(@PathVariable String department) {
        try {
            byte[] pdfBytes = pdfExportService.exportDoctorsByDepartmentToPdf(department);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "doctors_" + department.replaceAll("\\s+", "_") + "_report_" + 
                java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error exporting doctors by department to PDF: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Export doctors to HTML (for preview)
     */
    @GetMapping("/staff-coordinator/doctors/export/html")
    public ResponseEntity<String> exportDoctorsToHtml() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            String htmlContent = pdfExportService.generateDoctorsHtml(doctors, "All Doctors Report");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_HTML);
            
            return new ResponseEntity<>(htmlContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error exporting doctors to HTML: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}