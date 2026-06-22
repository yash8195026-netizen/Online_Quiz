package quiz_application.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    /* =========================
       ERROR PAGE
    ========================= */

    @GetMapping("/500")
    public String serverError() {
        return "500";
    }
}