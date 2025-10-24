package com.example.healthcare_customercare.service;

import com.example.healthcare_customercare.entity.Doctor;
import com.example.healthcare_customercare.entity.SupportTicket;
import com.example.healthcare_customercare.entity.Feedback;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfExportService {
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private SupportTicketService supportTicketService;
    
    @Autowired
    private FeedbackService feedbackService;
    
    /**
     * Export all doctors to PDF
     * @return PDF as byte array
     */
    public byte[] exportDoctorsToPdf() throws IOException {
        List<Doctor> doctors = doctorService.getAllDoctors();
        return generateDoctorsPdf(doctors, "All Doctors Report");
    }
    
    /**
     * Export doctors by specialization to PDF
     * @param specialization Doctor specialization
     * @return PDF as byte array
     */
    public byte[] exportDoctorsBySpecializationToPdf(String specialization) throws IOException {
        List<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return generateDoctorsPdf(doctors, "Doctors Report - " + specialization);
    }
    
    /**
     * Export doctors by department to PDF
     * @param department Doctor department
     * @return PDF as byte array
     */
    public byte[] exportDoctorsByDepartmentToPdf(String department) throws IOException {
        List<Doctor> doctors = doctorService.getDoctorsByDepartment(department);
        return generateDoctorsPdf(doctors, "Doctors Report - " + department);
    }
    
    /**
     * Generate PDF for doctors list
     * @param doctors List of doctors
     * @param title Report title
     * @return PDF as byte array
     */
    private byte[] generateDoctorsPdf(List<Doctor> doctors, String title) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {
            
            // Set up fonts
            PdfFont headerFont = PdfFontFactory.createFont();
            PdfFont bodyFont = PdfFontFactory.createFont();
            
            // Add title
            Paragraph titleParagraph = new Paragraph(title)
                    .setFont(headerFont)
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(titleParagraph);
            
            // Add generation date
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            Paragraph dateParagraph = new Paragraph("Generated on: " + currentDate)
                    .setFont(bodyFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(30);
            document.add(dateParagraph);
            
            // Add hospital information
            Paragraph hospitalInfo = new Paragraph("Arogya Medical Center - MediFlow Connect")
                    .setFont(headerFont)
                    .setFontSize(14)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(hospitalInfo);
            
            if (doctors.isEmpty()) {
                Paragraph noDataParagraph = new Paragraph("No doctors found for the specified criteria.")
                        .setFont(bodyFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(50);
                document.add(noDataParagraph);
            } else {
                // Create table for doctor details
                Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 2, 2, 2}))
                        .setWidth(UnitValue.createPercentValue(100))
                        .setMarginTop(20);
                
                // Add table headers
                String[] headers = {"ID", "Name", "Specialization", "Department", "Email"};
                for (String header : headers) {
                    Cell headerCell = new Cell()
                            .add(new Paragraph(header)
                                    .setFont(headerFont)
                                    .setBold()
                                    .setFontSize(12))
                            .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setPadding(8);
                    table.addHeaderCell(headerCell);
                }
                
                // Add doctor data
                for (Doctor doctor : doctors) {
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(doctor.getDocId()))
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6)
                            .setTextAlignment(TextAlignment.CENTER));
                    
                    table.addCell(new Cell().add(new Paragraph(doctor.getDoctorName() != null ? doctor.getDoctorName() : "N/A")
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6));
                    
                    table.addCell(new Cell().add(new Paragraph(doctor.getSpecialization() != null ? doctor.getSpecialization() : "N/A")
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6));
                    
                    table.addCell(new Cell().add(new Paragraph(doctor.getDepartment() != null ? doctor.getDepartment() : "N/A")
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6));
                    
                    table.addCell(new Cell().add(new Paragraph(doctor.getEmail() != null ? doctor.getEmail() : "N/A")
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6));
                }
                
                document.add(table);
                
                // Add summary
                Paragraph summaryParagraph = new Paragraph("Total Doctors: " + doctors.size())
                        .setFont(bodyFont)
                        .setFontSize(12)
                        .setBold()
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setMarginTop(20);
                document.add(summaryParagraph);
            }
            
            // Add footer
            Paragraph footerParagraph = new Paragraph("This report was generated by MediFlow Connect System")
                    .setFont(bodyFont)
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(50);
            document.add(footerParagraph);
            
        }
        
        return outputStream.toByteArray();
    }
    
    /**
     * Generate HTML content for doctors (alternative method)
     * @param doctors List of doctors
     * @param title Report title
     * @return HTML content
     */
    public String generateDoctorsHtml(List<Doctor> doctors, String title) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>");
        html.append("<html><head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>").append(title).append("</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append(".header { text-align: center; margin-bottom: 30px; }");
        html.append(".title { font-size: 24px; font-weight: bold; color: #0d9488; }");
        html.append(".subtitle { font-size: 14px; color: #666; margin-top: 10px; }");
        html.append(".hospital { font-size: 18px; font-weight: bold; color: #0891b2; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
        html.append("th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }");
        html.append("th { background-color: #f0fdfa; font-weight: bold; }");
        html.append("tr:nth-child(even) { background-color: #f9f9f9; }");
        html.append(".summary { text-align: right; margin-top: 20px; font-weight: bold; }");
        html.append(".footer { text-align: center; margin-top: 50px; font-size: 10px; color: #666; }");
        html.append("</style>");
        html.append("</head><body>");
        
        // Header
        html.append("<div class='header'>");
        html.append("<div class='title'>").append(title).append("</div>");
        html.append("<div class='hospital'>Arogya Medical Center - MediFlow Connect</div>");
        html.append("<div class='subtitle'>Generated on: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("</div>");
        html.append("</div>");
        
        if (doctors.isEmpty()) {
            html.append("<p style='text-align: center; margin-top: 50px;'>No doctors found for the specified criteria.</p>");
        } else {
            // Table
            html.append("<table>");
            html.append("<thead><tr>");
            html.append("<th>ID</th>");
            html.append("<th>Name</th>");
            html.append("<th>Specialization</th>");
            html.append("<th>Department</th>");
            html.append("<th>Email</th>");
            html.append("</tr></thead>");
            html.append("<tbody>");
            
            for (Doctor doctor : doctors) {
                html.append("<tr>");
                html.append("<td>").append(doctor.getDocId()).append("</td>");
                html.append("<td>").append(doctor.getDoctorName() != null ? doctor.getDoctorName() : "N/A").append("</td>");
                html.append("<td>").append(doctor.getSpecialization() != null ? doctor.getSpecialization() : "N/A").append("</td>");
                html.append("<td>").append(doctor.getDepartment() != null ? doctor.getDepartment() : "N/A").append("</td>");
                html.append("<td>").append(doctor.getEmail() != null ? doctor.getEmail() : "N/A").append("</td>");
                html.append("</tr>");
            }
            
            html.append("</tbody></table>");
            
            // Summary
            html.append("<div class='summary'>Total Doctors: ").append(doctors.size()).append("</div>");
        }
        
        // Footer
        html.append("<div class='footer'>This report was generated by MediFlow Connect System</div>");
        html.append("</body></html>");
        
        return html.toString();
    }
    
    /**
     * Convert HTML to PDF using iText HTML2PDF
     * @param htmlContent HTML content
     * @return PDF as byte array
     */
    public byte[] convertHtmlToPdf(String htmlContent) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            HtmlConverter.convertToPdf(htmlContent, outputStream);
        } catch (Exception e) {
            throw new IOException("Error converting HTML to PDF: " + e.getMessage(), e);
        }
        
        return outputStream.toByteArray();
    }
    
    /**
     * Export support tickets to PDF
     * @return PDF as byte array
     */
    public byte[] exportTicketsToPDF() throws IOException {
        List<SupportTicket> tickets = supportTicketService.getAllTickets();
        return generateTicketsPdf(tickets, "Support Tickets Report");
    }
    
    /**
     * Export feedback to PDF
     * @return PDF as byte array
     */
    public byte[] exportFeedbackToPDF() throws IOException {
        List<Feedback> feedbacks = feedbackService.getAllFeedback();
        return generateFeedbackPdf(feedbacks, "Customer Feedback Report");
    }
    
    /**
     * Generate PDF for support tickets
     * @param tickets List of support tickets
     * @param title Report title
     * @return PDF as byte array
     */
    private byte[] generateTicketsPdf(List<SupportTicket> tickets, String title) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {
            
            // Set up fonts
            PdfFont headerFont = PdfFontFactory.createFont();
            PdfFont bodyFont = PdfFontFactory.createFont();
            
            // Add title
            Paragraph titleParagraph = new Paragraph(title)
                    .setFont(headerFont)
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(titleParagraph);
            
            // Add generation date
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            Paragraph dateParagraph = new Paragraph("Generated on: " + currentDate)
                    .setFont(bodyFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(30);
            document.add(dateParagraph);
            
            // Add hospital information
            Paragraph hospitalInfo = new Paragraph("Arogya Medical Center - MediFlow Connect")
                    .setFont(headerFont)
                    .setFontSize(14)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(hospitalInfo);
            
            if (tickets.isEmpty()) {
                Paragraph noDataParagraph = new Paragraph("No support tickets found.")
                        .setFont(bodyFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(50);
                document.add(noDataParagraph);
            } else {
                // Create table for ticket details
                Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 2, 2, 2}))
                        .setWidth(UnitValue.createPercentValue(100))
                        .setMarginTop(20);
                
                // Add table headers
                String[] headers = {"ID", "Subject", "Status", "Priority", "Created Date"};
                for (String header : headers) {
                    Cell headerCell = new Cell()
                            .add(new Paragraph(header)
                                    .setFont(headerFont)
                                    .setBold()
                                    .setFontSize(12))
                            .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setPadding(8);
                    table.addHeaderCell(headerCell);
                }
                
                // Add ticket data
                for (SupportTicket ticket : tickets) {
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(ticket.getTicketId()))
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6)
                            .setTextAlignment(TextAlignment.CENTER));
                    
                    table.addCell(new Cell().add(new Paragraph(ticket.getSubject() != null ? ticket.getSubject() : "N/A")
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6));
                    
                    table.addCell(new Cell().add(new Paragraph(ticket.getStatus() != null ? ticket.getStatus() : "N/A")
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6));
                    
                    table.addCell(new Cell().add(new Paragraph(ticket.getPriority() != null ? ticket.getPriority() : "N/A")
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6));
                    
                    table.addCell(new Cell().add(new Paragraph(ticket.getCreatedAt() != null ? ticket.getCreatedAt().toString() : "N/A")
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6));
                }
                
                document.add(table);
                
                // Add summary
                Paragraph summaryParagraph = new Paragraph("Total Tickets: " + tickets.size())
                        .setFont(bodyFont)
                        .setFontSize(12)
                        .setBold()
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setMarginTop(20);
                document.add(summaryParagraph);
            }
            
            // Add footer
            Paragraph footerParagraph = new Paragraph("This report was generated by MediFlow Connect System")
                    .setFont(bodyFont)
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(50);
            document.add(footerParagraph);
            
        }
        
        return outputStream.toByteArray();
    }
    
    /**
     * Generate PDF for feedback
     * @param feedbacks List of feedback
     * @param title Report title
     * @return PDF as byte array
     */
    private byte[] generateFeedbackPdf(List<Feedback> feedbacks, String title) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try (PdfWriter writer = new PdfWriter(outputStream);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {
            
            // Set up fonts
            PdfFont headerFont = PdfFontFactory.createFont();
            PdfFont bodyFont = PdfFontFactory.createFont();
            
            // Add title
            Paragraph titleParagraph = new Paragraph(title)
                    .setFont(headerFont)
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(titleParagraph);
            
            // Add generation date
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            Paragraph dateParagraph = new Paragraph("Generated on: " + currentDate)
                    .setFont(bodyFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(30);
            document.add(dateParagraph);
            
            // Add hospital information
            Paragraph hospitalInfo = new Paragraph("Arogya Medical Center - MediFlow Connect")
                    .setFont(headerFont)
                    .setFontSize(14)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(hospitalInfo);
            
            if (feedbacks.isEmpty()) {
                Paragraph noDataParagraph = new Paragraph("No feedback found.")
                        .setFont(bodyFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(50);
                document.add(noDataParagraph);
            } else {
                // Create table for feedback details
                Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 2, 2, 2}))
                        .setWidth(UnitValue.createPercentValue(100))
                        .setMarginTop(20);
                
                // Add table headers
                String[] headers = {"ID", "User Email", "Rating", "Type", "Created Date"};
                for (String header : headers) {
                    Cell headerCell = new Cell()
                            .add(new Paragraph(header)
                                    .setFont(headerFont)
                                    .setBold()
                                    .setFontSize(12))
                            .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setPadding(8);
                    table.addHeaderCell(headerCell);
                }
                
                // Add feedback data
                for (Feedback feedback : feedbacks) {
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(feedback.getFeedbackId()))
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6)
                            .setTextAlignment(TextAlignment.CENTER));
                    
                    table.addCell(new Cell().add(new Paragraph(feedback.getUserEmail() != null ? feedback.getUserEmail() : "N/A")
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6));
                    
                    table.addCell(new Cell().add(new Paragraph(feedback.getRating() != null ? feedback.getRating().toString() : "N/A")
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6));
                    
                    table.addCell(new Cell().add(new Paragraph(feedback.getFeedbackType() != null ? feedback.getFeedbackType() : "N/A")
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6));
                    
                    table.addCell(new Cell().add(new Paragraph(feedback.getCreatedAt() != null ? feedback.getCreatedAt().toString() : "N/A")
                            .setFont(bodyFont)
                            .setFontSize(10))
                            .setPadding(6));
                }
                
                document.add(table);
                
                // Add summary
                Paragraph summaryParagraph = new Paragraph("Total Feedback: " + feedbacks.size())
                        .setFont(bodyFont)
                        .setFontSize(12)
                        .setBold()
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setMarginTop(20);
                document.add(summaryParagraph);
            }
            
            // Add footer
            Paragraph footerParagraph = new Paragraph("This report was generated by MediFlow Connect System")
                    .setFont(bodyFont)
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(50);
            document.add(footerParagraph);
            
        }
        
        return outputStream.toByteArray();
    }
}
