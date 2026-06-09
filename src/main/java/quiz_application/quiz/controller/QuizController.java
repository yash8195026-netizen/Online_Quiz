package quiz_application.quiz.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import quiz_application.quiz.model.Question;
import quiz_application.quiz.service.QuestionsService;

@Controller
public class QuizController {

private final QuestionsService questionsService;

public QuizController(QuestionsService questionsService) {
    this.questionsService = questionsService;
}

@GetMapping("/home")
public String homePage() {
    return "home";
}

@GetMapping("/quizList")
public String quizList(Model model) {
    model.addAttribute("questions", questionsService.loadQuizzes());
    return "quizList";
}

/* =========================
   ADD QUIZ
   ========================= */

@GetMapping("/addQuiz")
public String addQuizPage(Model model) {
    model.addAttribute("question", new Question());
    return "addQuiz";
}

@PostMapping("/addQuiz")
public String addQuiz(
        @ModelAttribute Question question,
        @RequestParam String option1,
        @RequestParam String option2,
        @RequestParam String option3,
        @RequestParam String option4) {

    question.setOption1(option1);
    question.setOption2(option2);
    question.setOption3(option3);
    question.setOption4(option4);

    questionsService.addQuiz(question);
    return "redirect:/quizList";
}

/* =========================
   EDIT QUIZ
   ========================= */

@GetMapping("/editQuiz/{id}")
public String editQuizPage(@PathVariable Long id, Model model) {

    Question question = questionsService.findById(id);
    model.addAttribute("question", question);
    return "editQuiz";
}

@PutMapping("/editQuiz")
public String editQuiz(
        @ModelAttribute Question question,
        @RequestParam String option1,
        @RequestParam String option2,
        @RequestParam String option3,
        @RequestParam String option4) {

    question.setOption1(option1);
    question.setOption2(option2);
    question.setOption3(option3);
    question.setOption4(option4);

    questionsService.editQuiz(question);
    return "redirect:/quizList";
}

/* =========================
   DELETE QUIZ
   ========================= */

@DeleteMapping("/deleteQuiz/{id}")
public String deleteQuiz(@PathVariable Long id) {
    questionsService.deleteQuiz(id);
    return "redirect:/quizList";
}

/* =========================
   TAKE QUIZ
   ========================= */

@GetMapping("/quiz")
public String quizPage(Model model, HttpSession session) {

    Long quizStartTime =(Long) session.getAttribute("quizStartTime");

    if (quizStartTime == null) {
        quizStartTime = System.currentTimeMillis();
        session.setAttribute("quizStartTime", quizStartTime);
    }
    long elapsed =(System.currentTimeMillis() - quizStartTime) / 1000;

    if (elapsed >= 600) {

        session.removeAttribute("quizStartTime");

        model.addAttribute("score", 0);
        model.addAttribute("totalQuestions",questionsService.loadQuizzes().size());
        model.addAttribute("percentage", 0);
        model.addAttribute("error", "Quiz time expired");

        return "result";
    }

    long remaining = Math.max(600 - elapsed, 0);

    model.addAttribute("quizTime", remaining);
    model.addAttribute("questions",questionsService.loadQuizzes());

    return "quiz";
}

/* =========================
   SUBMIT QUIZ
   ========================= */

@PostMapping("/submit")
public String submitAnswer(
        @RequestParam Map<String, String> answers,
        Model model,
        HttpSession session) {

    Long quizStartTime =(Long) session.getAttribute("quizStartTime");

    if (quizStartTime != null) {

        long elapsed =(System.currentTimeMillis() - quizStartTime) / 1000;

        if (elapsed >= 600) {

            session.removeAttribute("quizStartTime");

            model.addAttribute("score", 0);
            model.addAttribute("totalQuestions",questionsService.loadQuizzes().size());
            model.addAttribute("percentage", 0);
            model.addAttribute("error", "Quiz time expired");
            return "result";
        }
    }

    List<Question> questions = questionsService.loadQuizzes();
    int score = 0;

    for (Question q : questions) {
        String selectedAnswer = answers.get("question_" + q.getId());

        if (selectedAnswer != null &&
                selectedAnswer.equals(q.getCorrectAnswer())) {
            score++;
        }
    }
    int totalQuestions = questions.size();
    double percentage = totalQuestions == 0? 0: ((double) score / totalQuestions) * 100;

    model.addAttribute("score", score);
    model.addAttribute("totalQuestions", totalQuestions);
    model.addAttribute("percentage", percentage);
    session.removeAttribute("quizStartTime");
    return "result";
}

/* =========================
   RESULTS PAGE
   ========================= */

@GetMapping("/results")
public String resultPage(Model model) {
    model.addAttribute("score", 0);
    model.addAttribute("totalQuestions", 0);
    model.addAttribute("percentage", 0);
    return "result";
}
}
