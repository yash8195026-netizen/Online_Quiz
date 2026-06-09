package quiz_application.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quiz_application.quiz.model.Question;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    
}
