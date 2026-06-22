package quiz_application.quiz.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import quiz_application.quiz.model.LeaderboardEntry;
import quiz_application.quiz.model.User;
import quiz_application.quiz.service.CategoryService;
import quiz_application.quiz.service.QuizResultService;
import quiz_application.quiz.service.QuizUserDetailsService;

@Controller
public class LeaderboardController {

    // =====================================================
    // DEPENDENCIES
    // =====================================================

    private final QuizResultService quizResultService;
    private final QuizUserDetailsService userService;
    private final CategoryService categoryService;

    public LeaderboardController(
            QuizResultService quizResultService,
            QuizUserDetailsService userService,
            CategoryService categoryService) {

        this.quizResultService = quizResultService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    // =====================================================
    // LEADERBOARD PAGE
    // =====================================================

    /**
     * Display leaderboard by category.
     */
    @GetMapping("/leaderboard")
    public String leaderboard(
            @RequestParam(defaultValue = "java") String category,
            Model model) {

        List<LeaderboardEntry> leaderboard =
                quizResultService
                        .getLeaderboardByCategory(category)
                        .stream()
                        .map(result -> {

                            User user =
                                    userService.findById(
                                            result.getUserId());

                            if (user == null
                                    || "ADMIN".equalsIgnoreCase(
                                            user.getRole())) {

                                return null;
                            }

                            return new LeaderboardEntry(
                                    user.getUsername(),
                                    result.getCategory(),
                                    result.getScore(),
                                    result.getTotalQuestions(),
                                    result.getPercentage());
                        })
                        .filter(Objects::nonNull)
                        .toList();

        model.addAttribute(
                "leaderboard",
                leaderboard);

        model.addAttribute(
                "selectedCategory",
                category);

        model.addAttribute(
                "categories",
                categoryService.getAllCategories());

        return "leaderboard";
    }
}