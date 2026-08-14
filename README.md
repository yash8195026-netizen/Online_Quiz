# 🎯 Online Quiz Application

A full-stack Quiz Management System built using **Java, Spring Boot, Spring Security, Thymeleaf, PostgreSQL, and Render**.

The application allows users to register, attempt quizzes by category, track their performance, view leaderboards, and manage profiles. Administrators can manage quiz questions, categories, and monitor platform activity through an admin dashboard.

---

## 🌐 Live Demo

**Website:** https://online-quiz-yetw.onrender.com/

---

## 🚀 Features

### 👤 User Features

* User Registration & Login
* Secure Authentication with Spring Security
* Email OTP Verification
* Forgot Password using Email OTP
* Change Email using Email OTP
* Profile Management
* Category-wise Quiz System
* One Attempt per Category Rule
* Quiz Review Page
* Quiz History Tracking
* Leaderboard Ranking
* Dark Mode Support
* Responsive Mobile-Friendly Design

### 🛠️ Admin Features

* Admin Dashboard
* Add Quiz Questions
* Edit Quiz Questions
* Delete Quiz Questions
* Manage Categories
* View Quiz Statistics
* Monitor User Activity

---

## 🏗️ Tech Stack

### Backend

* Java 21
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate

### Frontend

* HTML5
* CSS3
* JavaScript
* Thymeleaf

### Database

* PostgreSQL (Production)
* MySQL (Development)

### Email Services

The application supports **two email delivery methods**:

* **Gmail SMTP** – Recommended for local development
* **Brevo API** – Recommended for cloud deployment such as Render

Both options can be used to send:

* Registration OTP
* Forgot Password OTP
* Change Email OTP

Only **one email provider needs to be configured at a time**.

### Tools & Deployment

* Git
* GitHub
* Maven
* Render
* pgAdmin
* MySQL Workbench

---

## 🔒 Security Features

* BCrypt Password Encryption
* Role-Based Access Control (RBAC)
* Session-Based Authentication
* Protected Admin Routes
* Email OTP Verification
* Secure Password Reset Flow
* Environment Variables for Sensitive Credentials
* API Keys and Database Credentials excluded from source code

---

## 📂 Project Structure

```text
src/main/java
├── controller
├── service
├── repository
├── model
├── config

src/main/resources
├── templates
├── static/css
├── static/js
├── application.properties
├── application-prod.properties
```

## 🗄️ Database Tables

```text
users
categories
questions
quiz_results
```

## 📸 Application Modules

### Home Page

* Category Selection
* Quiz Availability Status
* Navigation Menu

### Quiz Module

* Multiple Choice Questions
* Timer Support
* Auto Evaluation

### Leaderboard

* Category-wise Ranking
* Top Performers

### History

* Previous Quiz Attempts
* Score Tracking

### Admin Dashboard

* User Statistics
* Question Management
* Category Management

### Email & OTP Module

* Registration OTP
* Forgot Password OTP
* Change Email OTP
* OTP Expiration
* OTP Resend Support
* Secure 6-Digit OTP Generation

---

## ⚙️ Local Setup

### 1. Clone Repository

```bash
git clone https://github.com/yash8195026-netizen/Online_Quiz.git
```

### 2. Navigate to Project

```bash
cd Online_Quiz
```

### 3. Configure Database

Update:

```text
src/main/resources/application.properties
```

with your local database credentials.

### 4. Configure Email Provider

The application supports **Gmail SMTP** and **Brevo API**.

Choose **one** provider.

#### Option 1: Gmail SMTP

Configure the following properties:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-gmail@gmail.com
spring.mail.password=your-gmail-app-password

spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

For Gmail, use a **Google App Password** rather than your normal Gmail password.

#### Option 2: Brevo API

Configure the Brevo API key as an environment variable:

```text
BREVO_API_KEY=your-brevo-api-key
```

The Brevo sender email must be verified in the Brevo account.

> **Recommended for Render:** Brevo API, because cloud hosting environments may restrict direct SMTP connections.

### 5. Run Application

```bash
mvn spring-boot:run
```

### 6. Open Browser

```text
http://localhost:8080
```

---

## ☁️ Production Deployment

The production application is deployed using **Render**.

Production uses:

```text
Spring Boot
     │
     ├── Neon PostgreSQL
     │
     └── Email Provider
             ├── Brevo API
             └── Gmail SMTP
```

### Render Environment Variables

For the database:

```text
DB_URL=your-postgresql-url
DB_USERNAME=your-database-username
DB_PASSWORD=your-database-password
```

For Brevo:

```text
BREVO_API_KEY=your-brevo-api-key
```

For Gmail SMTP, configure the corresponding mail properties/environment variables if Gmail is selected.

```text
SPRING_PROFILES_ACTIVE=prod
```

> **Security:** Never commit API keys, database passwords, Gmail App Passwords, or other sensitive credentials to GitHub. Use environment variables instead.

---

## 📧 Email Provider Comparison

| Provider   | Local Development | Render Deployment    | Configuration      |
| ---------- | ----------------- | -------------------- | ------------------ |
| Gmail SMTP | ✅                 | ⚠️ May be restricted | Gmail App Password |
| Brevo API  | ✅                 | ✅ Recommended        | Brevo API Key      |

The application is designed so that the email provider can be changed without changing the OTP functionality itself.

---

## 🎓 Learning Outcomes

This project helped me gain hands-on experience with:

* Spring Boot Application Development
* Spring Security Authentication
* Database Design & Management
* PostgreSQL Deployment
* REST API Integration
* Transactional Email Integration
* Responsive UI Development
* Git & GitHub Workflow
* Production Deployment using Render
* Environment-Based Application Configuration

---

## 👨‍💻 Author

**Yeshpal Singh**

Java Full Stack Developer

GitHub: https://github.com/yash8195026-netizen

---

## 📄 License

This project is licensed under the MIT License.
