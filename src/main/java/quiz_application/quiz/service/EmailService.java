package quiz_application.quiz.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Email Service
 *
 * Responsible for sending emails such as:
 * - Registration OTP
 * - Password Reset OTP
 * - Email Change OTP
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(
            JavaMailSender mailSender) {

        this.mailSender = mailSender;
    }

    /**
     * Send a simple text email.
     *
     * @param to      Recipient email
     * @param subject Email subject
     * @param body    Email body
     */
    public void sendEmail(
            String to,
            String subject,
            String body) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}