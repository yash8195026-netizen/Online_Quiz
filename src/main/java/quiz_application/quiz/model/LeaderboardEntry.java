package quiz_application.quiz.model;

/**
 * Leaderboard DTO
 *
 * Used for displaying leaderboard data in the UI.
 *
 * This class is NOT a database entity.
 * It combines user and quiz result information
 * for leaderboard rendering.
 */
public class LeaderboardEntry {

    private final String username;
    private final String category;
    private final int score;
    private final int totalQuestions;
    private final double percentage;

    /**
     * Constructor
     */
    public LeaderboardEntry(
            String username,
            String category,
            int score,
            int totalQuestions,
            double percentage) {

        this.username = username;
        this.category = category;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.percentage = percentage;
    }

    // =====================================================
    // GETTERS
    // =====================================================

    public String getUsername() {
        return username;
    }

    public String getCategory() {
        return category;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public double getPercentage() {
        return percentage;
    }

    // =====================================================
    // DEBUGGING
    // =====================================================

    @Override
    public String toString() {
        return "LeaderboardEntry{" +
                "username='" + username + '\'' +
                ", category='" + category + '\'' +
                ", score=" + score +
                ", totalQuestions=" + totalQuestions +
                ", percentage=" + percentage +
                '}';
    }
}