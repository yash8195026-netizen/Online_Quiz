package quiz_application.quiz.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * User Entity
 *
 * Stores application users.
 *
 * Roles:
 * - USER
 * - ADMIN
 *
 * Also stores:
 * - Profile picture
 * - Pending email change
 * - Email OTP verification details
 */
@Entity
@Table(name = "users")
public class User {

    // =====================================================
    // PRIMARY KEY
    // =====================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =====================================================
    // BASIC USER INFORMATION
    // =====================================================

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    /**
     * ROLE_USER
     * ROLE_ADMIN
     */
    @Column(nullable = false, length = 20)
    private String role;

    // =====================================================
    // PROFILE
    // =====================================================

    @Column(length = 500)
    private String profilePicture;

    // =====================================================
    // EMAIL CHANGE OTP
    // =====================================================

    private String pendingEmail;

    private String emailOtp;

    private LocalDateTime emailOtpExpiry;

    // =====================================================
    // CONSTRUCTORS
    // =====================================================

    public User() {
    }

    public User(
            String username,
            String password,
            String email,
            String role) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(
            String username,
            String password,
            String email,
            String role,
            String profilePicture) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.profilePicture = profilePicture;
    }

    // =====================================================
    // GETTERS & SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(
            Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(
            String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(
            String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(
            String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(
            String role) {
        this.role = role;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(
            String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPendingEmail() {
        return pendingEmail;
    }

    public void setPendingEmail(
            String pendingEmail) {
        this.pendingEmail = pendingEmail;
    }

    public String getEmailOtp() {
        return emailOtp;
    }

    public void setEmailOtp(
            String emailOtp) {
        this.emailOtp = emailOtp;
    }

    public LocalDateTime getEmailOtpExpiry() {
        return emailOtpExpiry;
    }

    public void setEmailOtpExpiry(
            LocalDateTime emailOtpExpiry) {
        this.emailOtpExpiry = emailOtpExpiry;
    }

    // =====================================================
    // DEBUGGING
    // =====================================================

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                '}';
    }
}