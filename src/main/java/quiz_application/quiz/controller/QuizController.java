package quiz_application.quiz.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import quiz_application.quiz.model.Question;
import quiz_application.quiz.model.User;
import quiz_application.quiz.service.QuestionsService;
import quiz_application.quiz.service.QuizResultService;
import quiz_application.quiz.service.QuizUserDetailsService;

@Controller
public class QuizController {

    private final QuestionsService questionsService;
    private final QuizResultService quizResultService;
    private final QuizUserDetailsService userService;


    public QuizController(
            QuestionsService questionsService,
            QuizResultService quizResultService,
            QuizUserDetailsService userService) {

        this.questionsService = questionsService;
        this.quizResultService = quizResultService;
        this.userService = userService;
        }

    private List<Question> getQuestions(String category) {
        return questionsService.loadQuizzesByCategory(category);
        }

    /* =========================
       QUIZ PAGE
       ========================= */

    @GetMapping("/quiz/{category}")
    public String quizByCategory(
            @PathVariable String category,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response,
            Principal principal) {

        response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");

        response.setHeader("Pragma","no-cache");

        response.setDateHeader("Expires",0);

        category = category.trim().toLowerCase();

        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(principal.getName());

        if (!user.getRole().equalsIgnoreCase("ADMIN")) {

            if (!quizResultService.canAttemptQuiz(
                    user.getId(),
                    category)) {

                LocalDateTime nextAttempt =
                        quizResultService.getNextAttemptTime(
                                user.getId(),category);

                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern(
                                "dd MMM yyyy hh:mm a");

                redirectAttributes.addFlashAttribute(
                        "error","You can reattempt "
                                + category.toUpperCase()
                                + " quiz after "+ nextAttempt.format(formatter));

                return "redirect:/home";
            }
        }

        String selectedCategory =
                (String) session.getAttribute(
                        "selectedCategory");

        if (selectedCategory != null
                && !selectedCategory.equalsIgnoreCase(category)) {

            redirectAttributes.addFlashAttribute(
                    "error","You already have an active "
                    + selectedCategory.toUpperCase()+ " quiz. Finish it first.");

            return "redirect:/home";
        }

        if (selectedCategory == null) {
            session.setAttribute(
                    "selectedCategory",category);
        }

        if (session.getAttribute(
                "quizStartTime") == null) {

            session.setAttribute(
                    "quizStartTime",System.currentTimeMillis());
        }

        List<Question> questions = getQuestions(category);

        if (questions.isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "error","No questions available for "+ category.toUpperCase());

            return "redirect:/home";
        }

        Long quizDuration =(Long) session.getAttribute("quizDuration");

        if (quizDuration == null) {

            quizDuration =questions.size() * 45L;

            session.setAttribute("quizDuration",quizDuration);
        }

        Long startTime =
                (Long) session.getAttribute(
                        "quizStartTime");

        long elapsed =
                (System.currentTimeMillis()
                        - startTime) / 1000;

        long remainingTime =
                Math.max(
                        0,
                        quizDuration - elapsed);

        model.addAttribute("questions",questions);

        model.addAttribute("category",category);

        model.addAttribute("quizTime",remainingTime);

        return "quiz";
    }

    /* =========================
       SUBMIT QUIZ
       ========================= */

    @PostMapping("/submit")
    public String submitAnswer(
            @RequestParam Map<String, String> answers,
            Model model,
            HttpSession session,
            Principal principal) {

        Long quizStartTime = (Long) session.getAttribute("quizStartTime");

        Long quizDuration = (Long) session.getAttribute("quizDuration");

        boolean timeExpired = false;

        // ================= TIME CHECK =================
        if (quizStartTime != null && quizDuration != null) {

                long elapsed = (System.currentTimeMillis() - quizStartTime) / 1000;

                if (elapsed >= quizDuration) {
                        timeExpired = true;
                }
        }

        // ================= CATEGORY CHECK =================
        String category = (String) session.getAttribute("selectedCategory");

        if (category == null) {

                model.addAttribute("error","Session expired. Please start the quiz again.");

                return "result";
        }

        // ================= FETCH QUESTIONS =================
        List<Question> questions = getQuestions(category);

        int score = 0;

        // ================= CALCULATE SCORE =================
        for (Question q : questions) {

        String selectedAnswer = answers.get("question_" + q.getId());

                if (selectedAnswer != null
                        && selectedAnswer.equals(q.getCorrectAnswer())) {
                        score++;
                }
        }

        int totalQuestions = questions.size();

        double percentage =
                totalQuestions == 0
                        ? 0
                        : ((double) score / totalQuestions) * 100;

        // ================= SAVE RESULT =================
        if (principal != null) {

        User user = userService.findByUsername(principal.getName());

        quizResultService.saveResult(
                user.getId(),
                category,
                score,
                totalQuestions,
                percentage);
        }

        // ================= REVIEW DATA =================
        session.setAttribute("reviewQuestions", questions);
        session.setAttribute("reviewAnswers", answers);

        // ================= CLEAN SESSION =================
        clearQuizSession(session);

        // ================= MODEL ATTRIBUTES =================
        model.addAttribute("score", score);
        model.addAttribute("totalQuestions", totalQuestions);
        model.addAttribute("percentage", percentage);

        if (timeExpired) {
                model.addAttribute("error","Time expired. Quiz auto-submitted.");
        }

        return "result";
     }

    /* =========================
       REVIEW PAGE
       ========================= */
    @SuppressWarnings("unchecked")
    @GetMapping("/review")
    public String reviewQuiz(
            HttpSession session,
            Model model) {

        List<Question> questions = (List<Question>) session.getAttribute("reviewQuestions");

        Map<String, String> answers = (Map<String, String>) session.getAttribute("reviewAnswers");

        if (questions == null || answers == null) {
            return "redirect:/home";
        }

        model.addAttribute("questions",questions);

        model.addAttribute("answers",answers);

        session.removeAttribute("reviewQuestions");
        session.removeAttribute("reviewAnswers");

        return "quizReview";
    }

    /* =========================
       HISTORY PAGE
       ========================= */

    @GetMapping("/history")
    public String historyPage(
            Principal principal,
            Model model) {

        if (principal == null) {
            return "redirect:/login";
        }

        User user =userService.findByUsername(principal.getName());

        model.addAttribute("history",quizResultService.getHistory(user.getId()));

        return "history";
    }

    /* =========================
       RESULT PAGE
       ========================= */

    @GetMapping("/results")
    public String resultPage(
            Model model) {

        model.addAttribute("score", 0);
        model.addAttribute("totalQuestions", 0);
        model.addAttribute("percentage", 0);

        return "result";
    }

    /* =========================
       UTILITIES
       ========================= */

    private void clearQuizSession(
            HttpSession session) {

        session.removeAttribute("quizStartTime");
        session.removeAttribute("quizDuration");
        session.removeAttribute("selectedCategory");
    }
}