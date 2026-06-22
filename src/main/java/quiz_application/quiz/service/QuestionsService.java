package quiz_application.quiz.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import quiz_application.quiz.model.Question;
import quiz_application.quiz.repository.QuestionRepository;

/**
 * Questions Service
 *
 * Handles all business logic related
 * to quiz questions.
 */
@Service
public class QuestionsService {

    private final QuestionRepository questionRepository;

    public QuestionsService(
            QuestionRepository questionRepository) {

        this.questionRepository = questionRepository;
    }

    // =====================================================
    // LOAD QUESTIONS
    // =====================================================

    /**
     * Load all questions.
     */
    public List<Question> loadQuizzes() {

        return questionRepository.findAll();
    }

    /**
     * Load questions by category.
     */
    public List<Question> loadQuizzesByCategory(
            String categoryName) {

        return questionRepository.findByCategory_Name(
                categoryName);
    }

    /**
     * Load paginated questions by category.
     */
    public Page<Question> loadQuizzesByCategory(
            String categoryName,
            int page) {

        return questionRepository.findByCategory_Name(
                categoryName,
                PageRequest.of(page, 10));
    }

    // =====================================================
    // CRUD OPERATIONS
    // =====================================================

    /**
    * Save or update a question.
    */
    public void saveQuestion(Question question) {

        questionRepository.save(question);
    }

    /**
     * Delete question by ID.
     */
    public void deleteQuiz(
            Long id) {

        questionRepository.deleteById(id);
    }

    /**
     * Find question by ID.
     */
    public Question findById(
            Long id) {

        return questionRepository
                .findById(id)
                .orElse(null);
    }

    // =====================================================
    // DASHBOARD STATS
    // =====================================================

    /**
     * Total number of questions.
     */
    public long countQuestions() {

        return questionRepository.count();
    }
}