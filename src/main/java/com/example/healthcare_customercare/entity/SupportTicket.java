package com.example.healthcare_customercare.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
public class SupportTicket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long ticketId;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "category", nullable = false)
    private String category;
    
    @Column(name = "priority", nullable = false)
    private String priority;
    
    @Column(name = "subject", nullable = false)
    private String subject;
    
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "user_email", nullable = false)
    private String userEmail;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "reply", columnDefinition = "TEXT")
    private String reply;
    
    @Column(name = "reply_date")
    private LocalDateTime replyDate;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Default constructor
    public SupportTicket() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructor with required fields
    public SupportTicket(String category, String priority, String subject, String description, String userEmail) {
        this.category = category;
        this.priority = priority;
        this.subject = subject;
        this.description = description;
        this.userEmail = userEmail;
        this.status = "open"; // Default status
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getTicketId() {
        return ticketId;
    }
    
    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getUserEmail() {
        return userEmail;
    }
    
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getReply() {
        return reply;
    }
    
    public void setReply(String reply) {
        this.reply = reply;
    }
    
    public LocalDateTime getReplyDate() {
        return replyDate;
    }
    
    public void setReplyDate(LocalDateTime replyDate) {
        this.replyDate = replyDate;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "SupportTicket{" +
               "ticketId=" + ticketId +
               ", createdAt=" + createdAt +
               ", category='" + category + '\'' +
               ", priority='" + priority + '\'' +
               ", subject='" + subject + '\'' +
               ", description='" + description + '\'' +
               ", userEmail='" + userEmail + '\'' +
               ", status='" + status + '\'' +
               ", reply='" + reply + '\'' +
               ", replyDate=" + replyDate +
               ", updatedAt=" + updatedAt +
               '}';
    }
}
