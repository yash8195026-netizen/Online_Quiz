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
* Forgot Password using Email OTP
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

```properties
application.properties
```

with your database credentials.

### 4. Run Application

```bash
mvn spring-boot:run
```

### 5. Open Browser

```text
http://localhost:8080
```

---

## 🎓 Learning Outcomes

This project helped me gain hands-on experience with:

* Spring Boot Application Development
* Spring Security Authentication
* Database Design & Management
* PostgreSQL Deployment
* Responsive UI Development
* Git & GitHub Workflow
* Production Deployment using Render

---

## 👨‍💻 Author

**Yeshpal Singh**

Java Full Stack Developer

GitHub: https://github.com/yash8195026-netizen

---

## 📄 License

This project is licensed under the MIT License.
