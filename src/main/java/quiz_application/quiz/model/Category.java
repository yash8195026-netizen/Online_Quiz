package quiz_application.quiz.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Category Entity
 *
 * Represents a quiz category such as:
 * - Java
 * - SQL
 * - Python
 * - Spring Boot
 *
 * One Category can contain multiple Questions.
 */
@Entity
@Table(name = "categories")
public class Category {

    /**
     * Primary Key
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Category Name
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Questions belonging to this category
     */
    @OneToMany(
            mappedBy = "category",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    /**
     * Default Constructor
     */
    public Category() {
    }

    /**
     * Constructor with category name
     */
    public Category(String name) {
        this.name = name;
    }

    // =====================================================
    // GETTERS & SETTERS
    // =====================================================

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(
            List<Question> questions) {
        this.questions = questions;
    }

    // =====================================================
    // DEBUGGING
    // =====================================================

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}