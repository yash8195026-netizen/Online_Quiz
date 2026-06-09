package quiz_application.quiz.service;

import java.util.List;
import org.springframework.stereotype.Service;
import quiz_application.quiz.model.Question;
import quiz_application.quiz.repository.QuestionRepository;

@Service
public class QuestionsService {

    private final QuestionRepository questionRepository;
    
    public QuestionsService(QuestionRepository questionRepository){
        this.questionRepository= questionRepository;
    }

    public List<Question> loadQuizzes() {
        return questionRepository.findAll();
    }

    public void addQuiz(Question question) {
        questionRepository.save(question);
    }

     public void editQuiz(Question question) {
        questionRepository.save(question);
    }

    public void deleteQuiz(Long id) {
        questionRepository.deleteById(id);
    }

    public Question findById(Long id){
        return questionRepository.findById(id).orElse(null);
    }
    
}
