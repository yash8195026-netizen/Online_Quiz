package quiz_application.quiz.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import quiz_application.quiz.model.User;
import quiz_application.quiz.service.QuizUserDetailsService;

@Controller
public class ProfileController {

    private final QuizUserDetailsService userService;

    public ProfileController(QuizUserDetailsService userService){
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profilePage(Principal principal,Model model){

        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }
}