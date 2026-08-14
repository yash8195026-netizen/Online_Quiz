package quiz_application.quiz.service;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    private static final String BREVO_URL =
            "https://api.brevo.com/v3/smtp/email";

    private static final String SENDER_NAME =
            "Quiz Application";

    private static final String SENDER_EMAIL =
            "project.work81950@gmail.com";

    private final OkHttpClient client =
            new OkHttpClient();

    /**
     * Sends an email through Brevo API.
     *
     * @param to      recipient email address
     * @param subject email subject
     * @param body    email message
     */
    public void sendEmail(
            String to,
            String subject,
            String body) {

        String json = """
                {
                  "sender": {
                    "name": "%s",
                    "email": "%s"
                  },
                  "to": [
                    {
                      "email": "%s"
                    }
                  ],
                  "subject": "%s",
                  "textContent": "%s"
                }
                """.formatted(
                escapeJson(SENDER_NAME),
                escapeJson(SENDER_EMAIL),
                escapeJson(to),
                escapeJson(subject),
                escapeJson(body)
        );

        RequestBody requestBody =
                RequestBody.create(
                        json,
                        MediaType.parse("application/json"));

        Request request =
                new Request.Builder()
                        .url(BREVO_URL)
                        .addHeader(
                                "accept",
                                "application/json")
                        .addHeader(
                                "api-key",
                                apiKey)
                        .addHeader(
                                "content-type",
                                "application/json")
                        .post(requestBody)
                        .build();

        try (Response response =
                     client.newCall(request).execute()) {

            String responseBody =
                    response.body() != null
                            ? response.body().string()
                            : "";

            if (!response.isSuccessful()) {

                throw new RuntimeException(
                        "Unable to send email. " +
                        "Brevo returned HTTP "
                        + response.code()
                        + ". Response: "
                        + responseBody);
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to send email. "
                    + "Please try again later.",
                    e);
        }
    }

    /**
     * Safely escapes special characters
     * before adding values to JSON.
     */
    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }
}