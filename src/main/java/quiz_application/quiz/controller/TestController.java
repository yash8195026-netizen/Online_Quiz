package quiz_application.quiz.controller;

import java.net.Socket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/google-test")
@ResponseBody
public String test() {

    try (Socket socket = new Socket("google.com", 80)) {
        return "CONNECTED";
    } catch (Exception e) {
        return e.toString();
    }
}
}