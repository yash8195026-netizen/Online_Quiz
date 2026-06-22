package quiz_application.quiz.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import quiz_application.quiz.model.Category;
import quiz_application.quiz.model.Question;
import quiz_application.quiz.model.User;
import quiz_application.quiz.repository.CategoryRepository;
import quiz_application.quiz.service.CategoryService;
import quiz_application.quiz.service.QuestionsService;
import quiz_application.quiz.service.QuizResultService;
import quiz_application.quiz.service.QuizUserDetailsService;

import java.security.Principal;

@Controller
public class AdminController {

    // =====================================================
    // DEPENDENCIES
    // =====================================================

    private final QuestionsService questionsService;
    private final QuizResultService quizResultService;
    private final QuizUserDetailsService userService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    /**
    * Constructor Injection
    */

    public AdminController(
            QuestionsService questionsService,
            QuizResultService quizResultService,
            QuizUserDetailsService userService,
            CategoryService categoryService,
            CategoryRepository categoryRepository) {

        this.questionsService = questionsService;
        this.quizResultService = quizResultService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    // =====================================================
    // DASHBOARD
    // =====================================================

    @GetMapping("/dashboard")
    public String dashboard(
            Model model,
            Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        User user =
                userService.findByUsername(
                        principal.getName());

        if (!user.getRole().equalsIgnoreCase("ADMIN")) {
            return "redirect:/home";
        }

        model.addAttribute(
                "totalUsers",
                userService.countUsers());

        model.addAttribute(
                "totalQuestions",
                questionsService.countQuestions());

        model.addAttribute(
                "totalAttempts",
                quizResultService.countAttempts());

        model.addAttribute(
                "popularCategory",
                quizResultService.getMostPopularCategory());

        return "dashboard";
    }

    // =====================================================
    // QUIZ LIST
    // =====================================================

    @GetMapping("/quizList/{category}")
    public String quizListByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<Question> questionPage =
                questionsService.loadQuizzesByCategory(
                        category,
                        page);

        model.addAttribute(
                "questions",
                questionPage.getContent());

        model.addAttribute(
                "currentPage",
                page);

        model.addAttribute(
                "totalPages",
                questionPage.getTotalPages());

        model.addAttribute(
                "category",
                category);

        return "quizList";
    }

    // =====================================================
    // ADD QUIZ
    // =====================================================

    @GetMapping("/addQuiz")
    public String addQuizPage(
            Model model) {

        model.addAttribute(
                "question",
                new Question());

        model.addAttribute(
                "categories",
                categoryService.getAllCategories());

        return "addQuiz";
    }

    @PostMapping("/addQuiz")
    public String addQuiz(
            @ModelAttribute Question question,
            @RequestParam Long categoryId,
            @RequestParam String option1,
            @RequestParam String option2,
            @RequestParam String option3,
            @RequestParam String option4) {

        Category category =
        categoryRepository
                .findById(categoryId)
                .orElse(null);

        if (category == null) {
            return "redirect:/addQuiz";
        }

        question.setCategory(category);

        question.setOption1(option1);
        question.setOption2(option2);
        question.setOption3(option3);
        question.setOption4(option4);

        questionsService.saveQuestion(question);

        return "redirect:/addQuiz";
    }

    // =====================================================
    // EDIT QUIZ
    // =====================================================

    @GetMapping("/editQuiz/{id}")
    public String editQuizPage(
            @PathVariable Long id,
            Model model) {

        Question question =
                questionsService.findById(id);

        model.addAttribute(
                "question",
                question);

        model.addAttribute(
                "categories",
                categoryService.getAllCategories());

        return "editQuiz";
    }

    @PutMapping("/editQuiz")
    public String editQuiz(
            @ModelAttribute Question question,
            @RequestParam Long categoryId,
            @RequestParam String option1,
            @RequestParam String option2,
            @RequestParam String option3,
            @RequestParam String option4) {

        Category category =
                categoryRepository
                        .findById(categoryId)
                        .orElseThrow();

        question.setCategory(category);

        question.setOption1(option1);
        question.setOption2(option2);
        question.setOption3(option3);
        question.setOption4(option4);

        questionsService.saveQuestion(question);

        return "redirect:/quizList/"
                + category.getName();
    }

    // =====================================================
    // DELETE QUIZ
    // =====================================================

    @DeleteMapping("/deleteQuiz/{id}")
    public String deleteQuiz(
            @PathVariable Long id) {

        questionsService.deleteQuiz(id);

        return "redirect:/quizList";
    }
}