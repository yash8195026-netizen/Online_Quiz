package quiz_application.quiz.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import quiz_application.quiz.model.User;
import quiz_application.quiz.model.Category;
import quiz_application.quiz.service.CategoryService;
import quiz_application.quiz.service.QuizResultService;
import quiz_application.quiz.service.QuizUserDetailsService;

@Controller
public class HomeController {

    private final QuizResultService quizResultService;
    private final QuizUserDetailsService userService;
    private final CategoryService categoryService;

    public HomeController(
            QuizResultService quizResultService,
            QuizUserDetailsService userService,
            CategoryService categoryService) {

        this.quizResultService = quizResultService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    /* =========================
       HOME PAGE
       ========================= */

    @GetMapping("/home")
    public String homePage(
            Principal principal,
            Model model) {

        model.addAttribute(
                "categories",
                categoryService.getAllCategories());

        if (principal != null) {

            User user =
                    userService.findByUsername(
                            principal.getName());

            Map<String, Boolean> availability =
                    new HashMap<>();

            for (Category category :
                    categoryService.getAllCategories()) {

                availability.put(
                        category.getName(),
                        quizResultService.canAttemptQuiz(
                                user.getId(),
                                category.getName().toLowerCase()));
            }

            model.addAttribute(
                    "availability",
                    availability);
        }

        return "home";
    }
}