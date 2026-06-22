package quiz_application.quiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Password Configuration
 *
 * Provides a PasswordEncoder bean used throughout the application
 * for:
 * - User Registration
 * - Login Authentication
 * - Password Change
 * - Password Reset
 *
 * BCrypt automatically handles password salting and hashing.
 */
@Configuration
public class PasswordConfig {

    /**
     * Creates the application's password encoder.
     *
     * @return BCrypt Password Encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}