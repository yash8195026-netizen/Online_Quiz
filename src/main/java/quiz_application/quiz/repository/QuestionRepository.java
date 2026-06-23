package quiz_application.quiz.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import quiz_application.quiz.model.Question;

/**
 * Question Repository
 *
 * Handles all database operations
 * related to quiz questions.
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {

    /**
     * Load all questions
     * for a specific category.
     */
  
    List<Question> findByCategory_NameIgnoreCase(String categoryName);

    /**
     * Load paginated questions
     * for a specific category.
     */
    Page<Question> findByCategory_NameIgnoreCase(
        String categoryName,
        Pageable pageable);
        
}