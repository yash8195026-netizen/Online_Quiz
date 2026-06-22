package quiz_application.quiz.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Quiz Result Entity
 *
 * Stores a user's quiz attempt result.
 *
 * Contains:
 * - User ID
 * - Category
 * - Score
 * - Total Questions
 * - Percentage
 * - Attempt Date
 */
@Entity
@Table(name = "quiz_results")
public class QuizResult {

    /**
     * Primary Key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User who attempted the quiz
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * Quiz category
     */
    @Column(nullable = false)
    private String category;

    /**
     * User score
     */
    @Column(nullable = false)
    private int score;

    /**
     * Total questions in quiz
     */
    @Column(nullable = false)
    private int totalQuestions;

    /**
     * Percentage score
     */
    @Column(nullable = false)
    private double percentage;

    /**
     * Date and time of attempt
     */
    @Column(nullable = false)
    private LocalDateTime attemptDate;

    // =====================================================
    // GETTERS & SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(
            Long userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(
            String category) {
        this.category = category;
    }

    public int getScore() {
        return score;
    }

    public void setScore(
            int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(
            int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(
            double percentage) {
        this.percentage = percentage;
    }

    public LocalDateTime getAttemptDate() {
        return attemptDate;
    }

    public void setAttemptDate(
            LocalDateTime attemptDate) {
        this.attemptDate = attemptDate;
    }

    // =====================================================
    // DEBUGGING
    // =====================================================

    @Override
    public String toString() {
        return "QuizResult{" +
                "id=" + id +
                ", userId=" + userId +
                ", category='" + category + '\'' +
                ", score=" + score +
                ", totalQuestions=" + totalQuestions +
                ", percentage=" + percentage +
                ", attemptDate=" + attemptDate +
                '}';
    }
}