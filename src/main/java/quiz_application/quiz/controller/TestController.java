package quiz_application.quiz.controller;

import java.net.Socket;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

   @GetMapping("/smtp465")
@ResponseBody
public String smtp465() {
    try (Socket socket = new Socket("smtp.gmail.com", 465)) {
        return "CONNECTED 465";
    } catch (Exception e) {
        e.printStackTrace();
        return e.toString();
    }
}
@GetMapping("/smtp587")
@ResponseBody
public String smtp587() {
    try (Socket socket = new Socket("smtp.gmail.com", 587)) {
        return "CONNECTED 587";
    } catch (Exception e) {
        e.printStackTrace();
        return e.toString();
    }
}
}