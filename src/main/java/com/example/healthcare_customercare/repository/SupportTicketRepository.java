package com.example.healthcare_customercare.repository;

import com.example.healthcare_customercare.entity.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    
    // Find tickets by user email
    List<SupportTicket> findByUserEmail(String userEmail);
    
    // Find tickets by category
    List<SupportTicket> findByCategory(String category);
    
    // Find tickets by priority
    List<SupportTicket> findByPriority(String priority);
    
    // Find tickets created within a date range
    List<SupportTicket> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Count tickets by priority
    long countByPriority(String priority);
    
    // Count tickets by category
    long countByCategory(String category);
    
    // Get ticket count by priority
    @Query("SELECT s.priority, COUNT(s) FROM SupportTicket s GROUP BY s.priority ORDER BY s.priority DESC")
    List<Object[]> getTicketCountByPriority();
    
    // Get ticket count by category
    @Query("SELECT s.category, COUNT(s) FROM SupportTicket s GROUP BY s.category")
    List<Object[]> getTicketCountByCategory();
    
    // Find recent tickets (e.g., last 30 days)
    List<SupportTicket> findByCreatedAtAfter(LocalDateTime dateTime);
    
    // Find tickets by subject containing text
    List<SupportTicket> findBySubjectContainingIgnoreCase(String subject);
}
