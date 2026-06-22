package quiz_application.quiz.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import quiz_application.quiz.model.QuizResult;

/**
 * Quiz Result Repository
 *
 * Handles database operations
 * related to quiz attempts and results.
 */
public interface QuizResultRepository
        extends JpaRepository<QuizResult, Long> {

    /**
     * Complete history of a user
     * ordered by latest attempt first.
     */
    List<QuizResult> findByUserIdOrderByAttemptDateDesc(
            Long userId);

    /**
     * Check whether a user
     * has attempted a category.
     */
    boolean existsByUserIdAndCategory(
            Long userId,
            String category);

    /**
     * Global top 10 results.
     */
    List<QuizResult> findTop10ByOrderByPercentageDesc();

    /**
     * Category leaderboard.
     */
    List<QuizResult> findByCategoryOrderByPercentageDesc(
            String category);

    /**
     * Get all results
     * for a category.
     */
    List<QuizResult> findByCategory(
            String category);

    /**
     * Latest attempt of a user
     * for a category.
     */
    Optional<QuizResult>
    findTopByUserIdAndCategoryOrderByAttemptDateDesc(
            Long userId,
            String category);
}