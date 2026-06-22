package quiz_application.quiz.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import quiz_application.quiz.model.QuizResult;
import quiz_application.quiz.repository.QuizResultRepository;

/**
 * Quiz Result Service
 *
 * Handles:
 * - Saving quiz results
 * - Quiz history
 * - Leaderboard
 * - Reattempt restrictions
 * - Dashboard statistics
 */
@Service
public class QuizResultService {

    private final QuizResultRepository quizResultRepository;

    public QuizResultService(
            QuizResultRepository quizResultRepository) {

        this.quizResultRepository = quizResultRepository;
    }

    // =====================================================
    // SAVE RESULT
    // =====================================================

    /**
     * Save a quiz attempt.
     */
    public void saveResult(
            Long userId,
            String category,
            int score,
            int totalQuestions,
            double percentage) {

        QuizResult result = new QuizResult();

        result.setUserId(userId);
        result.setCategory(category);
        result.setScore(score);
        result.setTotalQuestions(totalQuestions);
        result.setPercentage(percentage);
        result.setAttemptDate(LocalDateTime.now());

        quizResultRepository.save(result);
    }

    // =====================================================
    // HISTORY
    // =====================================================

    /**
     * Get user's quiz history.
     */
    public List<QuizResult> getHistory(
            Long userId) {

        return quizResultRepository
                .findByUserIdOrderByAttemptDateDesc(userId);
    }

    // =====================================================
    // ATTEMPT CHECKS
    // =====================================================

    /**
     * Check whether user already attempted
     * a category.
     */
    public boolean hasAttempted(
            Long userId,
            String category) {

        return quizResultRepository
                .existsByUserIdAndCategory(
                        userId,
                        category);
    }

    /**
     * Get latest attempt for category.
     */
    public QuizResult getLastAttempt(
            Long userId,
            String category) {

        return quizResultRepository
                .findTopByUserIdAndCategoryOrderByAttemptDateDesc(
                        userId,
                        category)
                .orElse(null);
    }

    /**
     * User can attempt again only after 24 hours.
     */
    public boolean canAttemptQuiz(
            Long userId,
            String category) {

        QuizResult lastAttempt =
                getLastAttempt(userId, category);

        if (lastAttempt == null) {
            return true;
        }

        LocalDateTime nextAllowed =
                lastAttempt.getAttemptDate()
                           .plusHours(24);

        return LocalDateTime.now()
                            .isAfter(nextAllowed);
    }

    /**
     * Get next allowed attempt time.
     */
    public LocalDateTime getNextAttemptTime(
            Long userId,
            String category) {

        QuizResult lastAttempt =
                getLastAttempt(userId, category);

        if (lastAttempt == null) {
            return null;
        }

        return lastAttempt.getAttemptDate()
                          .plusHours(24);
    }

    // =====================================================
    // LEADERBOARD
    // =====================================================

    /**
     * Global leaderboard.
     */
    public List<QuizResult> getLeaderboard() {

        return quizResultRepository
                .findTop10ByOrderByPercentageDesc();
    }

    /**
     * Category-wise leaderboard.
     *
     * Keeps only the best score
     * per user.
     */
    public List<QuizResult> getLeaderboardByCategory(
            String category) {

        List<QuizResult> results =
                quizResultRepository.findByCategory(category);

        return results.stream()
                .collect(Collectors.toMap(
                        QuizResult::getUserId,
                        result -> result,
                        (r1, r2) ->
                                r1.getPercentage() >= r2.getPercentage()
                                        ? r1
                                        : r2))
                .values()
                .stream()
                .sorted((a, b) ->
                        Double.compare(
                                b.getPercentage(),
                                a.getPercentage()))
                .toList();
    }

    // =====================================================
    // DASHBOARD STATISTICS
    // =====================================================

    /**
     * Total quiz attempts.
     */
    public long countAttempts() {

        return quizResultRepository.count();
    }

    /**
     * Most attempted category.
     */
    public String getMostPopularCategory() {

        List<QuizResult> results =
                quizResultRepository.findAll();

        if (results.isEmpty()) {
            return "N/A";
        }

        return results.stream()
                .collect(Collectors.groupingBy(
                        QuizResult::getCategory,
                        Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getKey()
                .toUpperCase();
    }
}