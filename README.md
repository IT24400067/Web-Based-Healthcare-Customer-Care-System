# Web-Based-Healthcare-Customer-Care-System

A comprehensive healthcare management system featuring role-based access control and real-time notifications using the Observer Design Pattern.

## 🏥 Project Overview

This is a web-based healthcare management platform that streamlines operations for medical centers. The system provides role-specific dashboards for customers, receptionists, staff coordinators, customer support managers, senior medical officers, and system administrators.

## ✨ Key Features

- **User Management**: Role-based authentication for 6 user types
- **Appointment System**: Online booking, scheduling, and management
- **Medical Records**: Digital storage and PDF export
- **Real-time Notifications**: Observer pattern implementation with WebSocket
- **Customer Support**: Ticket management and feedback system
- **Dashboard Interfaces**: Role-specific dashboards for all users
- **Security**: Secure authentication with password hashing

## 🛠️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.5.6
- **Database**: Microsoft SQL Server
- **Frontend**: Thymeleaf, Tailwind CSS, JavaScript
- **Build Tool**: Maven
- **Additional**: WebSocket, iText7 (PDF generation)

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Microsoft SQL Server
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/healthcare-customercare.git
   cd healthcare-customercare
   ```

2. **Database Setup**
   - Install Microsoft SQL Server
   - Create database named `health`
   - Run SQL scripts in order:
     ```sql
     -- Run these SQL files
     create_roles_table.sql
     create_medical_tables.sql
     create_notifications_table.sql
     sample_medical_data.sql
     ```

3. **Configure Database**
   - Update `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=health
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**
   - Open browser: `http://localhost:8080`

## 👥 User Roles

| Role | Description | Key Features |
|------|-------------|--------------|
| **Customer** | Patients | Book appointments, view medical records, submit feedback |
| **Receptionist** | Front desk staff | Manage appointments, register patients, room availability |
| **Staff Coordinator** | Administrative staff | Manage doctor schedules, staff assignment, room management |
| **Customer Support Manager** | Support staff | Handle support tickets, respond to feedback |
| **Senior Medical Officer** | Medical staff | Access patient history, manage medical reports |
| **System Admin** | IT administrator | User management, system configuration |
```

## 📁 Project Structure

src/main/java/com/example/healthcare_customercare/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── entity/          # JPA entities
├── observer/        # Observer pattern implementation
├── repository/      # Data repositories
├── service/         # Business logic services
└── strategy/        # Design pattern strategies

src/main/resources/
├── templates/       # Thymeleaf templates
├── static/          # Static resources
└── application.properties


