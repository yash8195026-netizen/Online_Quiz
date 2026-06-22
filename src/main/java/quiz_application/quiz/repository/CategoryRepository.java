package quiz_application.quiz.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import quiz_application.quiz.model.Category;

/**
 * Category Repository
 *
 * Handles all database operations
 * related to quiz categories.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Check whether a category exists.
     */
    boolean existsByName(String name);

    /**
     * Find category ignoring case.
     *
     * Example:
     * Java
     * java
     * JAVA
     */
    Optional<Category> findByNameIgnoreCase(
            String name);
}