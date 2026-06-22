package quiz_application.quiz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import quiz_application.quiz.model.User;

/**
 * User Repository
 *
 * Handles all database operations
 * related to application users.
 */
public interface UserRepository
        extends JpaRepository<User, Long> {

    /**
     * Find user by username.
     */
    Optional<User> findByUsername(
            String username);

    /**
     * Find user by email.
     */
    Optional<User> findByEmail(
            String email);

    /**
     * Check whether a username
     * already exists.
     */
    boolean existsByUsername(
            String username);

    /**
     * Check whether an email
     * already exists.
     */
    boolean existsByEmail(
            String email);
}