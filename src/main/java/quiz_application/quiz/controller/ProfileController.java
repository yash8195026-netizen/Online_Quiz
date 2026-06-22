package quiz_application.quiz.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import quiz_application.quiz.model.User;
import quiz_application.quiz.service.QuizUserDetailsService;

@Controller
public class ProfileController {

    // =====================================================
    // CONSTANTS
    // =====================================================

    private static final String UPLOAD_DIR = "uploads/";

    private static final long MAX_FILE_SIZE =
            5 * 1024 * 1024;

    // =====================================================
    // DEPENDENCIES
    // =====================================================

    private final QuizUserDetailsService userService;

    public ProfileController(
            QuizUserDetailsService userService) {

        this.userService = userService;
    }

    // =====================================================
    // PROFILE PAGE
    // =====================================================

    @GetMapping("/profile")
    public String profilePage(
            Principal principal,
            Model model) {

        User user =
                userService.findByUsername(
                        principal.getName());

        model.addAttribute("user", user);

        return "profile";
    }

    // =====================================================
    // CHANGE USERNAME
    // =====================================================

    @GetMapping("/changeUsername")
    public String changeUsernamePage() {

        return "changeUsername";
    }

    @PostMapping("/changeUsername")
    public String changeUsername(
            @RequestParam String username,
            Principal principal,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {

            userService.updateUsername(
                    principal.getName(),
                    username);

            logoutUser(request, response);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Username Updated Successfully. Please login Again.");

            return "redirect:/login?usernameChanged";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());

            return "redirect:/changeUsername";
        }
    }

    // =====================================================
    // CHANGE PASSWORD
    // =====================================================

    @GetMapping("/changePassword")
    public String changePasswordPage() {

        return "changePassword";
    }

    @PostMapping("/changePassword")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Principal principal,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            HttpServletResponse response) {

        try {

            if (!newPassword.equals(confirmPassword)) {

                throw new RuntimeException(
                        "Passwords do not match");
            }

            userService.changePassword(
                    principal.getName(),
                    currentPassword,
                    newPassword);

            logoutUser(request, response);

            return "redirect:/login?passwordChanged";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());

            return "redirect:/changePassword";
        }
    }

    // =====================================================
    // CHANGE PROFILE PICTURE
    // =====================================================

    @GetMapping("/changeProfilePicture")
    public String changeProfilePicturePage() {

        return "changeProfilePicture";
    }

    @PostMapping("/changeProfilePicture")
    public String changeProfilePicture(
            @RequestParam("image") MultipartFile file,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {

            validateImage(file);

            // Current user
            User user =
                    userService.findByUsername(
                            principal.getName());

            // Old image stored in DB
            String oldImage =user.getProfilePicture();

            // Save new image
            String fileName = saveImage(file);

            // Update database
            userService.updateProfilePicture(principal.getName(),fileName);

            // Delete previous image from uploads folder
            if (oldImage != null
                    && !oldImage.isBlank()
                    && !oldImage.equals("default.png")) {

                Path oldImagePath = Paths.get(UPLOAD_DIR,oldImage);

                Files.deleteIfExists(oldImagePath);
            }

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Profile Picture Updated Successfully");

            return "redirect:/profile";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());

            return "redirect:/changeProfilePicture";
        }
    }

    // =====================================================
    // CHANGE EMAIL
    // =====================================================

    @GetMapping("/changeEmail")
    public String changeEmailPage() {

        return "changeEmail";
    }

    @PostMapping("/changeEmail")
    public String changeEmail(
            @RequestParam String email,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {

            userService.initiateEmailChange(
                    principal.getName(),
                    email.trim());

            redirectAttributes.addFlashAttribute(
                    "success",
                    "OTP Sent Successfully. It will expire in 5 minutes.");

            return "redirect:/verifyEmailOtp";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());

            return "redirect:/changeEmail";
        }
    }

    // =====================================================
    // VERIFY EMAIL OTP
    // =====================================================

    @GetMapping("/verifyEmailOtp")
    public String verifyEmailOtpPage() {

        return "verifyEmailOtp";
    }

    @PostMapping("/verifyEmailOtp")
    public String verifyEmailOtp(
            @RequestParam String otp,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {

            userService.verifyEmailChange(
                    principal.getName(),
                    otp);

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Email Updated Successfully");

            return "redirect:/profile";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());

            return "redirect:/verifyEmailOtp";
        }
    }

    // =====================================================
    // RESEND EMAIL OTP
    // =====================================================

    @GetMapping("/resendEmailOtp")
    public String resendOtp(
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {

            User user =
                    userService.findByUsername(
                            principal.getName());

            if (user.getPendingEmail() == null) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "No pending email change request found.");

                return "redirect:/changeEmail";
            }

            userService.initiateEmailChange(
                    principal.getName(),
                    user.getPendingEmail());

            redirectAttributes.addFlashAttribute(
                    "success",
                    "New OTP Sent Successfully");

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    e.getMessage());
        }

        return "redirect:/verifyEmailOtp";
    }

    // =====================================================
    // HELPER METHODS
    // =====================================================

    private void logoutUser(
            HttpServletRequest request,
            HttpServletResponse response) {

        new SecurityContextLogoutHandler()
                .logout(
                        request,
                        response,
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication());
    }

    private void validateImage(
            MultipartFile file) {

        if (file.isEmpty()) {

            throw new RuntimeException(
                    "Please select an image");
        }

        String contentType =
                file.getContentType();

        if (contentType == null
                || (!contentType.equals("image/jpeg")
                && !contentType.equals("image/png"))) {

            throw new RuntimeException(
                    "Only JPG and PNG images are allowed");
        }

        if (file.getSize() > MAX_FILE_SIZE) {

            throw new RuntimeException(
                    "Image size must be less than 5MB");
        }
    }

    private String saveImage(
            MultipartFile file) throws Exception {

        Path uploadPath =
                Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {

            Files.createDirectories(uploadPath);
        }

        String originalName =
                file.getOriginalFilename();

        if (originalName == null) {

            originalName = "image.png";
        }

        originalName =
                originalName.replaceAll(
                        "[^a-zA-Z0-9.-]",
                        "_");

        String lowerName =
                originalName.toLowerCase();

        if (!lowerName.endsWith(".jpg")
                && !lowerName.endsWith(".jpeg")
                && !lowerName.endsWith(".png")) {

            throw new RuntimeException(
                    "Only JPG and PNG images are allowed");
        }

        String fileName =
                System.currentTimeMillis()
                        + "_"
                        + originalName;

        Files.copy(
                file.getInputStream(),
                uploadPath.resolve(fileName),
                StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}