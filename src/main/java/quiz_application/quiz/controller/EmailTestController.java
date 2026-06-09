package quiz_application.quiz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import quiz_application.quiz.service.EmailService;

@RestController
public class EmailTestController {

    private final EmailService emailService;

    public EmailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/test-email")
    public String sendTestEmail() {

        emailService.sendEmail(
                "akash972360@gmail.com",
                "Spring Boot Test",
                "Congratulations! Email sending works."
        );

        return "Email Sent Successfully";
    }
}