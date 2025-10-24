package com.example.healthcare_customercare.controller;

import com.example.healthcare_customercare.entity.User;
import com.example.healthcare_customercare.entity.SupportTicket;
import com.example.healthcare_customercare.entity.Feedback;
import com.example.healthcare_customercare.service.UserService;
import com.example.healthcare_customercare.service.SupportTicketService;
import com.example.healthcare_customercare.service.FeedbackService;
import com.example.healthcare_customercare.service.PdfExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class CustomerSupportManagerController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private SupportTicketService supportTicketService;
    
    @Autowired
    private FeedbackService feedbackService;
    
    @Autowired
    private PdfExportService pdfExportService;

    /**
     * Display ticket reply page
     */
    @GetMapping("/ticket-reply.html")
    public String ticketReplyPage(
            @RequestParam Long ticketId,
            Model model,
            HttpSession session) {
        
        // Temporarily bypass session check for testing
        System.out.println("=== TICKET REPLY PAGE DEBUG ===");
        System.out.println("Accessing ticket reply page for ticket ID: " + ticketId);
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        // Check if user is logged in and has correct role
        if (loggedInUser == null || !"CUSTOMER_SUPPORT_MANAGER".equals(loggedInUser.getRole())) {
            System.out.println("No valid session, but allowing access for testing");
            // return "redirect:/login.html?error=access_denied";
        }
        
        // Get the ticket details
        Optional<SupportTicket> ticketOpt = supportTicketService.getTicketById(ticketId);
        if (ticketOpt.isEmpty()) {
            model.addAttribute("error", "Ticket not found");
            return "redirect:/customer-support-manager.html?error=ticket_not_found";
        }
        
        SupportTicket ticket = ticketOpt.get();
        model.addAttribute("ticket", ticket);
        model.addAttribute("supportManager", loggedInUser);
        
        return "Customer-Support-Manager/ticket-reply";
    }

    /**
     * Display Customer Support Manager dashboard
     */
    @GetMapping("/customer-support-manager.html")
    public String customerSupportManagerDashboard(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String success,
            @RequestParam(required = false) String error,
            Model model,
            HttpSession session) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        // If not in session, try to get from URL parameter (from login redirect)
        if (loggedInUser == null && email != null && !email.isEmpty()) {
            Optional<User> userOpt = userService.findByEmail(email);
            if (userOpt.isPresent()) {
                loggedInUser = userOpt.get();
                // Store in session for future requests
                session.setAttribute("loggedInUser", loggedInUser);
            }
        }
        
        // Check if user is logged in and has correct role
        if (loggedInUser == null || !"CUSTOMER_SUPPORT_MANAGER".equals(loggedInUser.getRole())) {
            return "redirect:/login.html?error=access_denied";
        }
        
        // Get support manager by email
        Optional<User> supportManagerOpt = userService.findByEmail(email != null ? email : loggedInUser.getEmail());
        if (supportManagerOpt.isEmpty()) {
            model.addAttribute("error", "Support manager not found");
            return "Customer-Support-Manager/customer-support-manager";
        }
        
        User supportManager = supportManagerOpt.get();
        model.addAttribute("supportManager", supportManager);
        
        // Add success/error messages
        if (success != null) {
            model.addAttribute("success", success);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }
        
        return "Customer-Support-Manager/customer-support-manager";
    }

    /**
     * API endpoint to reply to a support ticket
     */
    @PostMapping("/api/support-tickets/reply")
    @ResponseBody
    public ApiResponse replyToTicket(@RequestBody Map<String, Object> replyData, HttpSession session) {
        try {
            // Temporarily bypass session check for testing
            System.out.println("=== REPLY API DEBUG ===");
            System.out.println("Processing reply request...");
            
            // Check if user is logged in and has correct role
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null || !"CUSTOMER_SUPPORT_MANAGER".equals(loggedInUser.getRole())) {
                System.out.println("No valid session, but allowing reply for testing");
                // return new ApiResponse(false, "Access denied", null);
                // Create a dummy user for testing
                loggedInUser = new User();
                loggedInUser.setEmail("test-support-manager@example.com");
            }
            
            Long ticketId = Long.valueOf(replyData.get("ticketId").toString());
            String replyMessage = replyData.get("replyMessage").toString();
            String newStatus = replyData.get("newStatus").toString();
            
            // Get the ticket
            Optional<SupportTicket> ticketOpt = supportTicketService.getTicketById(ticketId);
            if (ticketOpt.isEmpty()) {
                return new ApiResponse(false, "Ticket not found", null);
            }
            
            SupportTicket ticket = ticketOpt.get();
            
            // Update ticket status and save reply directly to the ticket
            if (newStatus != null && !newStatus.isEmpty()) {
                System.out.println("=== UPDATING TICKET STATUS ===");
                System.out.println("Old status: " + ticket.getStatus());
                System.out.println("New status: " + newStatus);
                ticket.setStatus(newStatus);
                System.out.println("=============================");
            }
            
            // Save the reply directly to the ticket
            ticket.setReply(replyMessage);
            ticket.setReplyDate(LocalDateTime.now());
            SupportTicket updatedTicket = supportTicketService.updateTicket(ticket);
            
            System.out.println("=== TICKET REPLY SAVED ===");
            System.out.println("Ticket ID: " + ticketId);
            System.out.println("Reply from: " + loggedInUser.getEmail());
            System.out.println("Reply message: " + replyMessage);
            System.out.println("New status: " + newStatus);
            System.out.println("Replied at: " + updatedTicket.getReplyDate());
            System.out.println("=========================");
            
            return new ApiResponse(true, "Reply sent successfully", Map.of(
                "ticketId", ticketId,
                "replyMessage", replyMessage,
                "newStatus", newStatus,
                "repliedBy", loggedInUser.getEmail(),
                "repliedAt", updatedTicket.getReplyDate()
            ));
            
        } catch (Exception e) {
            System.out.println("Error replying to ticket: " + e.getMessage());
            return new ApiResponse(false, "Error sending reply: " + e.getMessage(), null);
        }
    }

    /**
     * API endpoint to update an existing reply
     */
    @PutMapping("/api/support-tickets/{ticketId}/reply")
    @ResponseBody
    public ApiResponse updateReply(@PathVariable Long ticketId, @RequestBody Map<String, Object> updateData, HttpSession session) {
        try {
            System.out.println("=== UPDATE REPLY API DEBUG ===");
            System.out.println("Processing reply update request for ticket ID: " + ticketId);
            
            // Check if user is logged in and has correct role
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null || !"CUSTOMER_SUPPORT_MANAGER".equals(loggedInUser.getRole())) {
                System.out.println("No valid session, but allowing update for testing");
                // Create a dummy user for testing
                loggedInUser = new User();
                loggedInUser.setEmail("test-support-manager@example.com");
            }
            
            String updatedReplyMessage = updateData.get("replyMessage").toString();
            String newStatus = updateData.get("newStatus") != null ? updateData.get("newStatus").toString() : null;
            
            // Get the ticket
            Optional<SupportTicket> ticketOpt = supportTicketService.getTicketById(ticketId);
            if (ticketOpt.isEmpty()) {
                return new ApiResponse(false, "Ticket not found", null);
            }
            
            SupportTicket ticket = ticketOpt.get();
            
            // Check if there's an existing reply to update
            if (ticket.getReply() == null || ticket.getReply().trim().isEmpty()) {
                return new ApiResponse(false, "No existing reply to update", null);
            }
            
            // Update the reply and set updated timestamp
            ticket.setReply(updatedReplyMessage);
            ticket.setUpdatedAt(LocalDateTime.now());
            
            // Update status if provided
            if (newStatus != null && !newStatus.isEmpty()) {
                System.out.println("=== UPDATING TICKET STATUS IN UPDATE REPLY ===");
                System.out.println("Old status: " + ticket.getStatus());
                System.out.println("New status: " + newStatus);
                ticket.setStatus(newStatus);
                System.out.println("=============================================");
            }
            
            SupportTicket updatedTicket = supportTicketService.updateTicket(ticket);
            
            System.out.println("=== REPLY UPDATED ===");
            System.out.println("Ticket ID: " + ticketId);
            System.out.println("Updated by: " + loggedInUser.getEmail());
            System.out.println("Updated reply: " + updatedReplyMessage);
            System.out.println("Updated at: " + updatedTicket.getUpdatedAt());
            System.out.println("===================");
            
            return new ApiResponse(true, "Reply updated successfully", Map.of(
                "ticketId", ticketId,
                "updatedReplyMessage", updatedReplyMessage,
                "newStatus", updatedTicket.getStatus(),
                "updatedBy", loggedInUser.getEmail(),
                "updatedAt", updatedTicket.getUpdatedAt()
            ));
            
        } catch (Exception e) {
            System.out.println("Error updating reply: " + e.getMessage());
            return new ApiResponse(false, "Error updating reply: " + e.getMessage(), null);
        }
    }

    /**
     * API endpoint to delete a reply from a support ticket
     */
    @DeleteMapping("/api/support-tickets/{ticketId}/reply")
    @ResponseBody
    public ApiResponse deleteReply(@PathVariable Long ticketId, HttpSession session) {
        try {
            System.out.println("=== DELETE REPLY API DEBUG ===");
            System.out.println("Processing reply deletion request for ticket ID: " + ticketId);
            
            // Check if user is logged in and has correct role
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null || !"CUSTOMER_SUPPORT_MANAGER".equals(loggedInUser.getRole())) {
                System.out.println("No valid session, but allowing deletion for testing");
                // Create a dummy user for testing
                loggedInUser = new User();
                loggedInUser.setEmail("test-support-manager@example.com");
            }
            
            // Get the ticket
            Optional<SupportTicket> ticketOpt = supportTicketService.getTicketById(ticketId);
            if (ticketOpt.isEmpty()) {
                return new ApiResponse(false, "Ticket not found", null);
            }
            
            SupportTicket ticket = ticketOpt.get();
            
            // Check if there's a reply to delete
            if (ticket.getReply() == null || ticket.getReply().trim().isEmpty()) {
                return new ApiResponse(false, "No reply to delete", null);
            }
            
            // Clear the reply and related fields
            ticket.setReply(null);
            ticket.setReplyDate(null);
            ticket.setUpdatedAt(null);
            // Reset status to open when deleting reply
            ticket.setStatus("open");
            
            SupportTicket updatedTicket = supportTicketService.updateTicket(ticket);
            
            System.out.println("=== REPLY DELETED ===");
            System.out.println("Ticket ID: " + ticketId);
            System.out.println("Deleted by: " + loggedInUser.getEmail());
            System.out.println("Status reset to: " + updatedTicket.getStatus());
            System.out.println("===================");
            
            return new ApiResponse(true, "Reply deleted successfully", Map.of(
                "ticketId", ticketId,
                "deletedBy", loggedInUser.getEmail(),
                "statusResetTo", updatedTicket.getStatus()
            ));
            
        } catch (Exception e) {
            System.out.println("Error deleting reply: " + e.getMessage());
            return new ApiResponse(false, "Error deleting reply: " + e.getMessage(), null);
        }
    }

    /**
     * API endpoint to get a specific ticket by ID
     */
    @GetMapping("/api/support-tickets/{ticketId}")
    @ResponseBody
    public ApiResponse getTicketById(@PathVariable Long ticketId, HttpSession session) {
        try {
            // Temporarily bypass session check for testing
            System.out.println("=== SINGLE TICKET API DEBUG ===");
            System.out.println("Fetching ticket ID: " + ticketId);
            
            Optional<SupportTicket> ticketOpt = supportTicketService.getTicketById(ticketId);
            if (ticketOpt.isEmpty()) {
                return new ApiResponse(false, "Ticket not found", null);
            }
            
            SupportTicket ticket = ticketOpt.get();
            
            System.out.println("=== SINGLE TICKET DEBUG ===");
            System.out.println("Ticket ID: " + ticketId);
            System.out.println("Found ticket: " + ticket.getSubject());
            System.out.println("Status: " + ticket.getStatus());
            System.out.println("========================");
            
            return new ApiResponse(true, "Ticket retrieved successfully", ticket);
            
        } catch (Exception e) {
            System.out.println("Error retrieving ticket: " + e.getMessage());
            return new ApiResponse(false, "Error retrieving ticket: " + e.getMessage(), null);
        }
    }


    /**
     * API endpoint to get all support tickets
     */
    @GetMapping("/api/support-tickets/all")
    @ResponseBody
    public ApiResponse getAllSupportTickets(HttpSession session) {
        try {
            // Temporarily bypass session check for testing
            System.out.println("=== TICKETS API DEBUG ===");
            System.out.println("Fetching all support tickets...");
            
            List<SupportTicket> tickets = supportTicketService.getAllTickets();
            System.out.println("Retrieved " + tickets.size() + " tickets from database");
            
            // Log ticket details for debugging
            for (SupportTicket ticket : tickets) {
                System.out.println("Ticket ID: " + ticket.getTicketId() + 
                                 ", Subject: " + ticket.getSubject() + 
                                 ", Status: " + ticket.getStatus() + 
                                 ", User: " + ticket.getUserEmail());
            }
            
            return new ApiResponse(true, "Tickets retrieved successfully", tickets);
        } catch (Exception e) {
            System.out.println("Error retrieving tickets: " + e.getMessage());
            e.printStackTrace();
            return new ApiResponse(false, "Error retrieving tickets: " + e.getMessage(), null);
        }
    }


    /**
     * API endpoint to get all feedback
     */
    @GetMapping("/api/feedback/all")
    @ResponseBody
    public ApiResponse getAllFeedback(HttpSession session) {
        try {
            // Temporarily bypass session check for testing
            System.out.println("=== FEEDBACK API DEBUG ===");
            System.out.println("Fetching all feedback...");
            
            List<Feedback> feedback = feedbackService.getAllFeedback();
            System.out.println("Retrieved " + feedback.size() + " feedback entries from database");
            
            // Log feedback details for debugging
            for (Feedback fb : feedback) {
                System.out.println("Feedback ID: " + fb.getFeedbackId() + 
                                 ", Type: " + fb.getFeedbackType() + 
                                 ", Rating: " + fb.getRating() + 
                                 ", User: " + fb.getUserEmail());
            }
            
            return new ApiResponse(true, "Feedback retrieved successfully", feedback);
        } catch (Exception e) {
            System.out.println("Error retrieving feedback: " + e.getMessage());
            e.printStackTrace();
            return new ApiResponse(false, "Error retrieving feedback: " + e.getMessage(), null);
        }
    }

    /**
     * API endpoint to get feedback by ID
     */
    @GetMapping("/api/feedback/{id}")
    @ResponseBody
    public ApiResponse getFeedbackById(@PathVariable Long id, HttpSession session) {
        try {
            // Check if user is logged in and has correct role
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null || !"CUSTOMER_SUPPORT_MANAGER".equals(loggedInUser.getRole())) {
                return new ApiResponse(false, "Access denied", null);
            }
            
            Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
            if (feedback.isPresent()) {
                return new ApiResponse(true, "Feedback found", feedback.get());
            } else {
                return new ApiResponse(false, "Feedback not found", null);
            }
        } catch (Exception e) {
            return new ApiResponse(false, "Error retrieving feedback: " + e.getMessage(), null);
        }
    }

    /**
     * Test endpoint to check sample data
     */
    @GetMapping("/api/test-data")
    @ResponseBody
    public ApiResponse testSampleData(HttpSession session) {
        try {
            // Check if user is logged in and has correct role
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null || !"CUSTOMER_SUPPORT_MANAGER".equals(loggedInUser.getRole())) {
                return new ApiResponse(false, "Access denied", null);
            }
            
            long ticketCount = supportTicketService.getAllTickets().size();
            long feedbackCount = feedbackService.getAllFeedback().size();
            
            String message = String.format("Sample data loaded: %d tickets, %d feedback entries", ticketCount, feedbackCount);
            Map<String, Long> data = new HashMap<>();
            data.put("tickets", ticketCount);
            data.put("feedback", feedbackCount);
            return new ApiResponse(true, message, data);
        } catch (Exception e) {
            return new ApiResponse(false, "Error checking sample data: " + e.getMessage(), null);
        }
    }

    /**
     * Export support tickets to PDF
     */
    @GetMapping("/api/export/tickets/pdf")
    public void exportTicketsPDF(HttpServletResponse response, HttpSession session) {
        try {
            // Check if user is logged in and has correct role
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null || !"CUSTOMER_SUPPORT_MANAGER".equals(loggedInUser.getRole())) {
                System.out.println("No valid session, but allowing export for testing");
                // Create a dummy user for testing
                loggedInUser = new User();
                loggedInUser.setEmail("test-support-manager@example.com");
            }
            
            byte[] pdfBytes = pdfExportService.exportTicketsToPDF();
            
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=support-tickets-" + 
                LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".pdf");
            response.setContentLength(pdfBytes.length);
            
            response.getOutputStream().write(pdfBytes);
            response.getOutputStream().flush();
            
            System.out.println("=== TICKETS PDF EXPORTED ===");
            System.out.println("Exported by: " + loggedInUser.getEmail());
            System.out.println("PDF size: " + pdfBytes.length + " bytes");
            System.out.println("============================");
            
        } catch (Exception e) {
            System.out.println("Error exporting tickets PDF: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Export customer feedback to PDF
     */
    @GetMapping("/api/export/feedback/pdf")
    public void exportFeedbackPDF(HttpServletResponse response, HttpSession session) {
        try {
            // Check if user is logged in and has correct role
            User loggedInUser = (User) session.getAttribute("loggedInUser");
            if (loggedInUser == null || !"CUSTOMER_SUPPORT_MANAGER".equals(loggedInUser.getRole())) {
                System.out.println("No valid session, but allowing export for testing");
                // Create a dummy user for testing
                loggedInUser = new User();
                loggedInUser.setEmail("test-support-manager@example.com");
            }
            
            byte[] pdfBytes = pdfExportService.exportFeedbackToPDF();
            
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=customer-feedback-" + 
                LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".pdf");
            response.setContentLength(pdfBytes.length);
            
            response.getOutputStream().write(pdfBytes);
            response.getOutputStream().flush();
            
            System.out.println("=== FEEDBACK PDF EXPORTED ===");
            System.out.println("Exported by: " + loggedInUser.getEmail());
            System.out.println("PDF size: " + pdfBytes.length + " bytes");
            System.out.println("=============================");
            
        } catch (Exception e) {
            System.out.println("Error exporting feedback PDF: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // Inner classes for API responses
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        // Getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }
    }

    public static class ReplyRequest {
        private Long ticketId;
        private String replyMessage;
        private String newStatus;

        // Getters and setters
        public Long getTicketId() { return ticketId; }
        public void setTicketId(Long ticketId) { this.ticketId = ticketId; }
        public String getReplyMessage() { return replyMessage; }
        public void setReplyMessage(String replyMessage) { this.replyMessage = replyMessage; }
        public String getNewStatus() { return newStatus; }
        public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
    }
}
