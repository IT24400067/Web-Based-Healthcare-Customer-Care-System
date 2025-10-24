package com.example.healthcare_customercare.config;

import com.example.healthcare_customercare.entity.Doctor;
import com.example.healthcare_customercare.entity.Room;
import com.example.healthcare_customercare.entity.DoctorSchedule;
import com.example.healthcare_customercare.entity.SupportTicket;
import com.example.healthcare_customercare.entity.Feedback;
import com.example.healthcare_customercare.entity.User;
import com.example.healthcare_customercare.repository.DoctorRepository;
import com.example.healthcare_customercare.repository.RoomRepository;
import com.example.healthcare_customercare.repository.DoctorScheduleRepository;
import com.example.healthcare_customercare.repository.UserRepository;
import com.example.healthcare_customercare.repository.SupportTicketRepository;
import com.example.healthcare_customercare.repository.FeedbackRepository;
import com.example.healthcare_customercare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private DoctorRepository doctorRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SupportTicketRepository supportTicketRepository;
    
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if doctors already exist
        if (doctorRepository.count() == 0) {
            // Add sample doctors
            Doctor doctor1 = new Doctor(
                "Dr. Sarah Johnson",
                "Cardiology",
                "Cardiology Department",
                "sarah.johnson@arogya.com"
            );
            
            Doctor doctor2 = new Doctor(
                "Dr. Michael Chen",
                "Pediatrics",
                "Pediatrics Department",
                "michael.chen@arogya.com"
            );
            
            Doctor doctor3 = new Doctor(
                "Dr. Emily Rodriguez",
                "General Medicine",
                "General Medicine Department",
                "emily.rodriguez@arogya.com"
            );
            
            Doctor doctor4 = new Doctor(
                "Dr. David Kim",
                "Orthopedics",
                "Orthopedics Department",
                "david.kim@arogya.com"
            );
            
            Doctor doctor5 = new Doctor(
                "Dr. Lisa Wang",
                "Dermatology",
                "Dermatology Department",
                "lisa.wang@arogya.com"
            );

            doctorRepository.save(doctor1);
            doctorRepository.save(doctor2);
            doctorRepository.save(doctor3);
            doctorRepository.save(doctor4);
            doctorRepository.save(doctor5);
            
            System.out.println("Sample doctors added to database!");
        } else {
            System.out.println("Doctors already exist in database. Count: " + doctorRepository.count());
        }
        
        // Check if rooms already exist
        if (roomRepository.count() == 0) {
            // Add sample rooms with different capacities
            Room room1 = new Room(10); // room_capacity = 10, current_capacity = 0, remaining_capacity = 10
            Room room2 = new Room(15); // room_capacity = 15, current_capacity = 0, remaining_capacity = 15
            Room room3 = new Room(20); // room_capacity = 20, current_capacity = 0, remaining_capacity = 20
            Room room4 = new Room(10); // room_capacity = 10, current_capacity = 0, remaining_capacity = 10
            Room room5 = new Room(12); // room_capacity = 12, current_capacity = 0, remaining_capacity = 12
            Room room6 = new Room(8);  // room_capacity = 8, current_capacity = 0, remaining_capacity = 8
            Room room7 = new Room(25); // room_capacity = 25, current_capacity = 0, remaining_capacity = 25
            Room room8 = new Room(6);  // room_capacity = 6, current_capacity = 0, remaining_capacity = 6
            Room room9 = new Room(18); // room_capacity = 18, current_capacity = 0, remaining_capacity = 18
            Room room10 = new Room(14); // room_capacity = 14, current_capacity = 0, remaining_capacity = 14

            roomRepository.save(room1);
            roomRepository.save(room2);
            roomRepository.save(room3);
            roomRepository.save(room4);
            roomRepository.save(room5);
            roomRepository.save(room6);
            roomRepository.save(room7);
            roomRepository.save(room8);
            roomRepository.save(room9);
            roomRepository.save(room10);
            
            System.out.println("Sample rooms added to database!");
        } else {
            System.out.println("Rooms already exist in database. Count: " + roomRepository.count());
        }
        
        // Check if doctor schedules already exist
        if (doctorScheduleRepository.count() == 0) {
            // Get all doctors and rooms
            java.util.List<Doctor> doctors = doctorRepository.findAll();
            java.util.List<Room> rooms = roomRepository.findAll();
            
            if (!doctors.isEmpty() && !rooms.isEmpty()) {
                // Create schedules for the next 7 days
                java.time.LocalDate today = java.time.LocalDate.now();
                
                for (int day = 0; day < 7; day++) {
                    java.time.LocalDate scheduleDate = today.plusDays(day);
                    
                    // Create schedules for each doctor
                    for (int i = 0; i < doctors.size() && i < rooms.size(); i++) {
                        Doctor doctor = doctors.get(i);
                        Room room = rooms.get(i);
                        
                        // Morning schedule (8:00 AM - 12:00 PM)
                        DoctorSchedule morningSchedule = new DoctorSchedule(
                            scheduleDate,
                            java.time.LocalTime.of(8, 0),
                            java.time.LocalTime.of(12, 0),
                            "Morning",
                            doctor.getDocId(),
                            room.getRoomId()
                        );
                        
                        // Afternoon schedule (1:00 PM - 5:00 PM)
                        DoctorSchedule afternoonSchedule = new DoctorSchedule(
                            scheduleDate,
                            java.time.LocalTime.of(13, 0),
                            java.time.LocalTime.of(17, 0),
                            "Afternoon",
                            doctor.getDocId(),
                            room.getRoomId()
                        );
                        
                        // Evening schedule (6:00 PM - 10:00 PM)
                        DoctorSchedule eveningSchedule = new DoctorSchedule(
                            scheduleDate,
                            java.time.LocalTime.of(18, 0),
                            java.time.LocalTime.of(22, 0),
                            "Evening",
                            doctor.getDocId(),
                            room.getRoomId()
                        );
                        
                        doctorScheduleRepository.save(morningSchedule);
                        doctorScheduleRepository.save(afternoonSchedule);
                        doctorScheduleRepository.save(eveningSchedule);
                    }
                }
                
                System.out.println("Sample doctor schedules added to database!");
            } else {
                System.out.println("Cannot create schedules: No doctors or rooms found!");
            }
        } else {
            System.out.println("Doctor schedules already exist in database. Count: " + doctorScheduleRepository.count());
        }
        
        // Check if Customer Support Manager user exists
        if (!userRepository.existsByEmailAndRole("support@arogya.com", "CUSTOMER_SUPPORT_MANAGER")) {
            try {
                userService.createCustomerSupportManager(
                    "Support", 
                    "Manager", 
                    "support@arogya.com", 
                    "555-0123", 
                    "support123"
                );
                System.out.println("Customer Support Manager user created!");
                System.out.println("Email: support@arogya.com");
                System.out.println("Password: support123");
            } catch (Exception e) {
                System.out.println("Error creating Customer Support Manager user: " + e.getMessage());
            }
        } else {
            System.out.println("Customer Support Manager user already exists!");
        }
        
        // Check if System Admin user exists
        if (!userRepository.existsByEmailAndRole("admin@arogya.com", "ADMIN")) {
            try {
                // Create admin user with plain text password
                User adminUser = new User(
                    "System", 
                    "Admin", 
                    "admin@arogya.com", 
                    "555-0000", 
                    "admin123", 
                    "ADMIN"
                );
                userRepository.save(adminUser);
                System.out.println("System Admin user created!");
                System.out.println("Email: admin@arogya.com");
                System.out.println("Password: admin123");
            } catch (Exception e) {
                System.out.println("Error creating System Admin user: " + e.getMessage());
            }
        } else {
            System.out.println("System Admin user already exists!");
        }
        
        // Create sample support tickets if none exist
        if (supportTicketRepository.count() == 0) {
            try {
                // Create sample customers first
                userService.createCustomer("John", "Doe", "john.doe@example.com", "555-0001", "customer123");
                userService.createCustomer("Jane", "Smith", "jane.smith@example.com", "555-0002", "customer123");
                userService.createCustomer("Mike", "Johnson", "mike.johnson@example.com", "555-0003", "customer123");
                
                // Create sample support tickets
                SupportTicket ticket1 = new SupportTicket(
                    "technical", "high", "Login Issues", 
                    "I'm unable to log into my account. Getting error message 'Invalid credentials' even though I'm sure my password is correct.",
                    "john.doe@example.com"
                );
                
                SupportTicket ticket2 = new SupportTicket(
                    "appointment", "medium", "Appointment Rescheduling", 
                    "I need to reschedule my appointment for next week. Can you help me find available slots?",
                    "jane.smith@example.com"
                );
                
                SupportTicket ticket3 = new SupportTicket(
                    "billing", "urgent", "Payment Not Processed", 
                    "I made a payment yesterday but it's not showing up in my account. Please investigate immediately.",
                    "mike.johnson@example.com"
                );
                
                SupportTicket ticket4 = new SupportTicket(
                    "general", "low", "General Inquiry", 
                    "What are your operating hours? I want to schedule an appointment but need to know when you're open.",
                    "john.doe@example.com"
                );
                
                SupportTicket ticket5 = new SupportTicket(
                    "complaint", "high", "Poor Service Experience", 
                    "I had a very disappointing experience during my last visit. The staff was unprofessional and the wait time was excessive.",
                    "jane.smith@example.com"
                );
                
                supportTicketRepository.save(ticket1);
                supportTicketRepository.save(ticket2);
                supportTicketRepository.save(ticket3);
                supportTicketRepository.save(ticket4);
                supportTicketRepository.save(ticket5);
                
                System.out.println("Sample support tickets created!");
            } catch (Exception e) {
                System.out.println("Error creating sample support tickets: " + e.getMessage());
            }
        } else {
            System.out.println("Support tickets already exist! Count: " + supportTicketRepository.count());
        }
        
        // Create sample feedback if none exist
        if (feedbackRepository.count() == 0) {
            try {
                // Create sample feedback
                Feedback feedback1 = new Feedback(
                    "Service", "The staff was very professional and helpful. Great experience overall!", 
                    5, "john.doe@example.com"
                );
                
                Feedback feedback2 = new Feedback(
                    "Appointment", "The appointment scheduling was smooth and the doctor was excellent.", 
                    4, "jane.smith@example.com"
                );
                
                Feedback feedback3 = new Feedback(
                    "Staff", "The receptionist was very friendly and made the check-in process easy.", 
                    5, "mike.johnson@example.com"
                );
                
                Feedback feedback4 = new Feedback(
                    "Facilities", "The waiting area is clean and comfortable. Good amenities available.", 
                    4, "john.doe@example.com"
                );
                
                Feedback feedback5 = new Feedback(
                    "Overall", "Overall experience was good, but the wait time could be improved.", 
                    3, "jane.smith@example.com"
                );
                
                Feedback feedback6 = new Feedback(
                    "Service", "Excellent service! Highly recommend this healthcare facility.", 
                    5, "mike.johnson@example.com"
                );
                
                feedbackRepository.save(feedback1);
                feedbackRepository.save(feedback2);
                feedbackRepository.save(feedback3);
                feedbackRepository.save(feedback4);
                feedbackRepository.save(feedback5);
                feedbackRepository.save(feedback6);
                
                System.out.println("Sample feedback created!");
            } catch (Exception e) {
                System.out.println("Error creating sample feedback: " + e.getMessage());
            }
        } else {
            System.out.println("Feedback already exists! Count: " + feedbackRepository.count());
        }
    }
}
