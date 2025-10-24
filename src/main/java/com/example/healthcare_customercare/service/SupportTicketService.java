package com.example.healthcare_customercare.service;

import com.example.healthcare_customercare.entity.SupportTicket;
import com.example.healthcare_customercare.repository.SupportTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SupportTicketService {
    
    @Autowired
    private SupportTicketRepository supportTicketRepository;
    
    /**
     * Create a new support ticket
     */
    public SupportTicket createTicket(String category, String priority, String subject, String description, String userEmail) {
        // Validate input parameters
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required");
        }
        
        if (priority == null || priority.trim().isEmpty()) {
            throw new IllegalArgumentException("Priority is required");
        }
        
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject is required");
        }
        
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
        
        if (userEmail == null || userEmail.trim().isEmpty() || !userEmail.contains("@")) {
            throw new IllegalArgumentException("Valid user email is required");
        }
        
        // Create new support ticket
        SupportTicket ticket = new SupportTicket(
            category.trim(),
            priority.trim(),
            subject.trim(),
            description.trim(),
            userEmail.trim()
        );
        
        // Save to database
        return supportTicketRepository.save(ticket);
    }
    
    /**
     * Get all support tickets
     */
    public List<SupportTicket> getAllTickets() {
        return supportTicketRepository.findAll();
    }
    
    /**
     * Get ticket by ID
     */
    public Optional<SupportTicket> getTicketById(Long id) {
        return supportTicketRepository.findById(id);
    }
    
    /**
     * Update support ticket
     */
    public SupportTicket updateTicket(SupportTicket ticket) {
        System.out.println("=== SUPPORT TICKET SERVICE UPDATE ===");
        System.out.println("Updating ticket ID: " + ticket.getTicketId());
        System.out.println("New status: " + ticket.getStatus());
        SupportTicket savedTicket = supportTicketRepository.save(ticket);
        System.out.println("Saved ticket status: " + savedTicket.getStatus());
        System.out.println("=====================================");
        return savedTicket;
    }
    
    /**
     * Get tickets by category
     */
    public List<SupportTicket> getTicketsByCategory(String category) {
        return supportTicketRepository.findByCategory(category);
    }
    
    /**
     * Get tickets by user email
     */
    public List<SupportTicket> getTicketsByUserEmail(String userEmail) {
        return supportTicketRepository.findByUserEmail(userEmail);
    }
    
    /**
     * Get tickets by priority
     */
    public List<SupportTicket> getTicketsByPriority(String priority) {
        return supportTicketRepository.findByPriority(priority);
    }
    
    /**
     * Delete ticket
     */
    public boolean deleteTicket(Long id) {
        if (supportTicketRepository.existsById(id)) {
            supportTicketRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Get ticket count by priority
     */
    public long getTicketCountByPriority(String priority) {
        return supportTicketRepository.countByPriority(priority);
    }


    
    /**
     * Get ticket count by category
     */
    public long getTicketCountByCategory(String category) {
        return supportTicketRepository.countByCategory(category);
    }
    
    
    /**
     * Get ticket count by priority
     */
    public List<Object[]> getTicketCountByPriority() {
        return supportTicketRepository.getTicketCountByPriority();
    }
    
    /**
     * Get ticket count by category
     */
    public List<Object[]> getTicketCountByCategory() {
        return supportTicketRepository.getTicketCountByCategory();
    }
    
    /**
     * Get recent tickets (e.g., last 30 days)
     */
    public List<SupportTicket> getRecentTickets(int days) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(days);
        return supportTicketRepository.findByCreatedAtAfter(thirtyDaysAgo);
    }
    
    /**
     * Search tickets by subject
     */
    public List<SupportTicket> searchTicketsBySubject(String subject) {
        return supportTicketRepository.findBySubjectContainingIgnoreCase(subject);
    }
}
