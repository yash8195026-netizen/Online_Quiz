package quiz_application.quiz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security Configuration
 *
 * Handles:
 * - Authentication
 * - Authorization
 * - Login
 * - Logout
 * - Access control
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final LoginSuccessHandler loginSuccessHandler;

    public WebSecurityConfig(
            LoginSuccessHandler loginSuccessHandler) {

        this.loginSuccessHandler = loginSuccessHandler;
    }

    /**
     * Authentication Manager Bean
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    /**
     * Security Filter Chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

            // =====================================================
            // AUTHORIZATION RULES
            // =====================================================
            .authorizeHttpRequests(auth -> auth

                // Public Pages
                .requestMatchers(
                        "/login",
                        "/register",
                        "/verifyOtp",
                        "/forgotPassword",
                        "/resetPasswordOtp",
                        "/resetPassword",

                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/uploads/**",

                        "/403",
                        "/404",
                        "/500"
                ).permitAll()

                // Admin Only Pages
                .requestMatchers(
                        "/quizList/**",
                        "/addQuiz",
                        "/dashboard",
                        "/editQuiz/**",
                        "/deleteQuiz/**",
                        "/categories/**"
                ).hasRole("ADMIN")

                // Logged-In Users
                .requestMatchers(
                        "/home",
                        "/quiz/**",
                        "/submit",
                        "/results",
                        "/review",
                        "/history",
                        "/leaderboard",

                        "/profile",
                        "/changeUsername",
                        "/changePassword",
                        "/changeEmail",
                        "/verifyEmailOtp",
                        "/resendEmailOtp",
                        "/changeProfilePicture"
                ).hasAnyRole("USER", "ADMIN")

                .anyRequest().authenticated()
            )

            // =====================================================
            // ACCESS DENIED PAGE
            // =====================================================
            .exceptionHandling(ex ->
                    ex.accessDeniedPage("/403"))

            // =====================================================
            // LOGIN CONFIGURATION
            // =====================================================
            .formLogin(form -> form
                    .loginPage("/login")
                    .successHandler(loginSuccessHandler)
                    .permitAll())

            // =====================================================
            // LOGOUT CONFIGURATION
            // =====================================================
            .logout(logout -> logout

                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")

                    .logoutSuccessUrl("/login?logout")

                    .permitAll());

        return http.build();
    }
}