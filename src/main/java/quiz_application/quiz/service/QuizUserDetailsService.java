package quiz_application.quiz.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.security.SecureRandom;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import quiz_application.quiz.model.User;
import quiz_application.quiz.repository.UserRepository;

/**
 * User Service + Spring Security Service
 *
 * Responsibilities:
 * - User Registration
 * - User Lookup
 * - Password Management
 * - Username Updates
 * - Profile Picture Updates
 * - Email Change Verification
 * - Spring Security Authentication
 */
@Service
public class QuizUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public QuizUserDetailsService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // =====================================================
    // USER REGISTRATION
    // =====================================================

    /**
     * Register user using fields.
     */
    public void registerUser(
            String username,
            String password,
            String email,
            String role) {

        User user = new User(
                username,
                passwordEncoder.encode(password),
                email,
                role);

        userRepository.save(user);
    }

    /**
     * Register user using User object.
     */
    public void registerUser(
            User user) {

        if (userRepository.existsByUsername(
                user.getUsername())) {

            throw new RuntimeException(
                    "Username Already Exists");
        }

        if (userRepository.existsByEmail(
                user.getEmail())) {

            throw new RuntimeException(
                    "Email Already Exists");
        }

        user.setPassword(
                passwordEncoder.encode(
                        user.getPassword()));

        userRepository.save(user);
    }

    // =====================================================
    // VALIDATION
    // =====================================================

    public boolean usernameExists(
            String username) {

        return userRepository.existsByUsername(
                username);
    }

    public boolean emailExists(
            String email) {

        return userRepository.existsByEmail(
                email);
    }

    // =====================================================
    // USER LOOKUP
    // =====================================================

    /**
     * Find user by username.
     */
    public User findByUsername(
            String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User Not Found"));
    }

    /**
     * Find user by ID.
     *
     * Returns null if not found.
     * This prevents leaderboard crashes
     * when a user is removed but quiz
     * history still exists.
     */
    public User findById(
            Long id) {

        return userRepository.findById(id)
                .orElse(null);
    }

    /**
     * Find user by email.
     */
    public User findByEmail(
            String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User Not Found"));
    }

    // =====================================================
    // PASSWORD MANAGEMENT
    // =====================================================

    /**
     * Reset password (Forgot Password flow).
     */
    public void updatePassword(
            String email,
            String newPassword) {

        User user = findByEmail(email);

        user.setPassword(
                passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }

    /**
     * Change username.
     */
    public void updateUsername(
            String currentUsername,
            String newUsername) {

        User user = findByUsername(currentUsername);

        if (!user.getUsername().equals(newUsername)
                && userRepository.existsByUsername(newUsername)) {

            throw new RuntimeException(
                    "Username Already Exists");
        }

        user.setUsername(newUsername);

        userRepository.save(user);
    }

    /**
     * Update profile picture filename.
     */
    public void updateProfilePicture(
            String username,
            String fileName) {

        User user = findByUsername(username);

        user.setProfilePicture(fileName);

        userRepository.save(user);
    }

    /**
     * Change password after verifying
     * current password.
     */
    public void changePassword(
            String username,
            String currentPassword,
            String newPassword) {

        User user = findByUsername(username);

        if (!passwordEncoder.matches(
                currentPassword,
                user.getPassword())) {

            throw new RuntimeException(
                    "Current password is incorrect");
        }

        user.setPassword(
                passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }
    // =====================================================
    // EMAIL CHANGE OTP
    // =====================================================

    /**
     * Generate OTP and send it to the new email.
     */
    public void initiateEmailChange(
            String username,
            String newEmail) {

        // Validate email format
        if (!newEmail.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            throw new RuntimeException(
                    "Invalid email format");
        }

        // Check duplicate email
        if (userRepository.existsByEmail(newEmail)) {

            throw new RuntimeException(
                    "Email Already Exists");
        }

        User user = findByUsername(username);

        // Generate 6-digit OTP
        SecureRandom random = new SecureRandom();

        String otp = String.valueOf(
                100000 + random.nextInt(900000));

        // Save temporary email and OTP
        user.setPendingEmail(newEmail);
        user.setEmailOtp(otp);

        // OTP valid for 5 minutes
        user.setEmailOtpExpiry(
                LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);

        try {

            emailService.sendEmail(
                    newEmail,
                    "Email Change OTP",
                    "Your OTP is: "
                            + otp
                            + "\n\nValid for 5 minutes.");

        } catch (Exception e) {

            // Rollback if email sending fails
            user.setPendingEmail(null);
            user.setEmailOtp(null);
            user.setEmailOtpExpiry(null);

            userRepository.save(user);

            throw new RuntimeException(
                    "Unable to send OTP. Please check the email address.");
        }
    }

    /**
     * Verify OTP and update email.
     */
    public void verifyEmailChange(
            String username,
            String otp) {

        User user = findByUsername(username);

        // OTP expired
        if (user.getEmailOtpExpiry() == null
                || LocalDateTime.now()
                .isAfter(user.getEmailOtpExpiry())) {

            throw new RuntimeException(
                    "OTP Expired");
        }

        // No OTP generated
        if (user.getEmailOtp() == null) {

            throw new RuntimeException(
                    "No OTP found. Please request a new OTP.");
        }

        // Invalid OTP
        if (!otp.equals(user.getEmailOtp())) {

            throw new RuntimeException(
                    "Invalid OTP");
        }

        // Update email
        user.setEmail(user.getPendingEmail());

        // Clear temporary values
        user.setPendingEmail(null);
        user.setEmailOtp(null);
        user.setEmailOtpExpiry(null);

        userRepository.save(user);
    }

    // =====================================================
    // STATISTICS
    // =====================================================

    /**
     * Total registered users.
     */
    public long countUsers() {

        return userRepository.count();
    }

    // =====================================================
    // SPRING SECURITY LOGIN
    // =====================================================

    /**
     * Login using username OR email.
     */
    @Override
    public UserDetails loadUserByUsername(
            String usernameOrEmail)
            throws UsernameNotFoundException {

        User user = userRepository
                .findByUsername(usernameOrEmail)
                .orElseGet(() ->
                        userRepository
                                .findByEmail(usernameOrEmail)
                                .orElseThrow(() ->
                                        new UsernameNotFoundException(
                                                "User Not Found")));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole())));
    }
}