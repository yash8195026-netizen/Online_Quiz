package quiz_application.quiz.controller;

import java.net.Socket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/smtp-test")
    public String smtpTest() {

        try (Socket socket = new Socket("smtp.gmail.com", 587)) {
            return "SMTP CONNECTED";
        } catch (Exception e) {
            return e.toString();
        }
    }
}