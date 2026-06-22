package quiz_application.quiz.model;

import jakarta.persistence.*;

/**
 * Question Entity
 *
 * Represents a single quiz question.
 *
 * Each question belongs to one category and contains:
 * - Question Text
 * - Four Options
 * - Correct Answer
 */
@Entity
@Table(name = "questions")
public class Question {

    /**
     * Primary Key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Category associated with this question
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false)
    private Category category;

    /**
     * Question text
     */
    @Column(
            nullable = false,
            columnDefinition = "TEXT")
    private String questionText;

    /**
     * Option 1
     */
    @Column(nullable = false)
    private String option1;

    /**
     * Option 2
     */
    @Column(nullable = false)
    private String option2;

    /**
     * Option 3
     */
    @Column(nullable = false)
    private String option3;

    /**
     * Option 4
     */
    @Column(nullable = false)
    private String option4;

    /**
     * Correct Answer
     */
    @Column(nullable = false)
    private String correctAnswer;

    /**
     * Default Constructor
     */
    public Question() {
    }

    // =====================================================
    // GETTERS & SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(
            Category category) {
        this.category = category;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(
            String questionText) {
        this.questionText = questionText;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(
            String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(
            String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(
            String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(
            String option4) {
        this.option4 = option4;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(
            String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    // =====================================================
    // DEBUGGING
    // =====================================================

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", category=" +
                (category != null ? category.getName() : "null") +
                '}';
    }
}