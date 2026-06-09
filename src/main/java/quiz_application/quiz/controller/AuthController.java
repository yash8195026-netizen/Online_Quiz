package quiz_application.quiz.controller;

import java.security.SecureRandom;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import quiz_application.quiz.model.User;
import quiz_application.quiz.service.EmailService;
import quiz_application.quiz.service.QuizUserDetailsService;

@Controller
public class AuthController {
private final QuizUserDetailsService userService;
private final EmailService emailService;

public AuthController(QuizUserDetailsService userService,EmailService emailService) {
    this.userService = userService;
    this.emailService = emailService;
}

private String generateOtp() {
    SecureRandom secureRandom = new SecureRandom();
    return String.valueOf(100000 + secureRandom.nextInt(900000));
}

@GetMapping("/login")
public String loginPage() {
    return "login";
}

@GetMapping("/register")
public String registerPage(Model model) {
    model.addAttribute("user", new User());
    return "register";
}

/* =========================
   FORGOT PASSWORD
   ========================= */

@GetMapping("/forgotPassword")
public String forgotPasswordPage() {
    return "forgotPassword";
}

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam String email,HttpSession session,Model model) {

        
        if (!userService.emailExists(email)) {
            model.addAttribute("error","Email not registered");
            return "forgotPassword";
        }

        try {

            String otp = generateOtp();

            session.setAttribute("resetOtp", otp);
            session.setAttribute("resetEmail", email);
            session.setAttribute("resetOtpTime",System.currentTimeMillis());

        emailService.sendEmail(email,"Password Reset OTP","Your OTP For Password Change is: " + otp);
        return "redirect:/resetPasswordOtp";

    } catch (Exception e) {

        model.addAttribute("error","Unable to send OTP");
        return "forgotPassword";
    }
}

@GetMapping("/resetPasswordOtp")
public String resetPasswordOtpPage(HttpSession session, Model model) {
    Long otpTime = (Long) session.getAttribute("resetOtpTime");
    if (otpTime != null) {
        long elapsed = (System.currentTimeMillis() - otpTime) / 1000;
        long remaining = Math.max(300 - elapsed, 0);
        model.addAttribute("remainingTime", remaining);
    }
    return "resetPasswordOtp";
}

@PostMapping("/resetPassword")
public String resetPassword(
        @RequestParam String otp,
        @RequestParam String newPassword,
        @RequestParam String confirmPassword,
        HttpSession session,
        Model model) {

    String storedOtp =(String) session.getAttribute("resetOtp");
    String email =(String) session.getAttribute("resetEmail");
    Long otpTime =(Long) session.getAttribute("resetOtpTime");

    if (storedOtp == null || email == null || otpTime == null) {
        return "redirect:/forgotPassword";
    }

    if (System.currentTimeMillis() - otpTime > 300000) {
        clearResetPasswordSession(session);
        model.addAttribute("error", "OTP expired");
        return "resetPasswordOtp";
    }

    if (!storedOtp.equals(otp)) {
        model.addAttribute("error","Invalid OTP");
        return "resetPasswordOtp";
    }

    if (!newPassword.equals(confirmPassword)) {
        model.addAttribute("error","Passwords do not match");
        return "resetPasswordOtp";
    }

    userService.updatePassword(email,newPassword);
    clearResetPasswordSession(session);

    return "redirect:/login?resetSuccess";
}

/* =========================
   REGISTRATION OTP
   ========================= */

@GetMapping("/verifyOtp")
public String verifyOtpPage(HttpSession session,Model model) {
    Long otpTime = (Long) session.getAttribute("otpTime");
    if(otpTime != null){
        long remaining = 300-((System.currentTimeMillis()-otpTime)/1000);
        model.addAttribute("remainingTime",Math.max(remaining, 0));
    }
    return "verifyOtp";
}

@PostMapping("/register")
public String registerUser(@ModelAttribute User user,Model model,HttpSession session) {

    try {
        if (userService.usernameExists(user.getUsername())) {
            model.addAttribute("error","Username already exists");
            return "register";
        }

        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("error","Email already exists");
            return "register";
        }
        String otp = generateOtp();
        session.setAttribute("pendingUser",user);
        session.setAttribute("otp",otp);
        session.setAttribute("otpTime",System.currentTimeMillis());
        session.setAttribute("otpAttempts",0);
        session.setAttribute("resendCount",0);

        emailService.sendEmail(user.getEmail(),"Quiz Application OTP Verification","Your OTP is: " + otp);
        return "redirect:/verifyOtp";

    } catch (Exception e) {
        model.addAttribute("error","Unable to send OTP");
        return "register";
    }
}

@PostMapping("/verifyOtp")
public String verifyOtp(@RequestParam String otp,HttpSession session,Model model) {
    String storedOtp =(String) session.getAttribute("otp");
    User pendingUser =(User) session.getAttribute("pendingUser");
    Long otpTime =(Long) session.getAttribute("otpTime");

    if (storedOtp == null || pendingUser == null || otpTime == null) {
        return "redirect:/register";
    }

    if (System.currentTimeMillis() - otpTime > 300000) {

        clearOtpSession(session);
        model.addAttribute("error","OTP expired. Please register again.");
        return "register";
    }

    Integer otpAttempts =(Integer) session.getAttribute("otpAttempts");

    if (otpAttempts == null) {
         otpAttempts = 0;
    }

    if (!storedOtp.equals(otp)) {
        otpAttempts++;
        session.setAttribute("otpAttempts",otpAttempts);

        if (otpAttempts >= 5) {
            clearOtpSession(session);
            model.addAttribute("error","Maximum OTP attempts reached. Please register again.");
            return "register";
        }
        model.addAttribute("error","Invalid OTP. Remaining attempts: "+ (5 - otpAttempts));
        return "verifyOtp";
    }

    try {
        pendingUser.setRole("USER");
        userService.registerUser(pendingUser);
    } catch (RuntimeException e) {
        model.addAttribute("error",e.getMessage());
        return "verifyOtp";
    }
    clearOtpSession(session);
    return "redirect:/login?success";
}

@PostMapping("/resendOtp")
public String resendOtp(HttpSession session,Model model) {

    Integer resendCount =(Integer) session.getAttribute("resendCount");
    User pendingUser =(User) session.getAttribute("pendingUser");

    if (pendingUser == null) {
        return "redirect:/register";
    }

    if (resendCount == null) {
        resendCount = 0;
    }

    if (resendCount >= 3) {

        model.addAttribute("error","Maximum resend attempts reached");
        return "verifyOtp";
    }

    String otp = generateOtp();
    session.setAttribute("otp",otp);
    session.setAttribute("otpTime",System.currentTimeMillis());
    session.setAttribute("resendCount",resendCount + 1);

    emailService.sendEmail(pendingUser.getEmail(),"Quiz Application OTP Verification","Your OTP is: " + otp);
    model.addAttribute("success","New OTP sent successfully");
    return "verifyOtp";
}

private void clearOtpSession(HttpSession session) {

    session.removeAttribute("otp");
    session.removeAttribute("pendingUser");
    session.removeAttribute("otpTime");
    session.removeAttribute("otpAttempts");
    session.removeAttribute("resendCount");
}

private void clearResetPasswordSession(
        HttpSession session) {

    session.removeAttribute("resetOtp");
    session.removeAttribute("resetEmail");
    session.removeAttribute("resetOtpTime");
}
}
