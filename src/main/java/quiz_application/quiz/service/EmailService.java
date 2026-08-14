package quiz_application.quiz.service;

import java.io.IOException;

import okhttp3.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY:}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient();

    public void sendEmail(
            String to,
            String subject,
            String body) {

        System.out.println("========== BREVO DEBUG ==========");
        System.out.println("Recipient : " + to);
        System.out.println("Subject   : " + subject);
        System.out.println(
                "API Key Loaded : "
                + (apiKey != null && !apiKey.isBlank()));

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException(
                    "BREVO_API_KEY is missing from environment variables");
        }

        String json = """
        {
          "sender": {
            "name": "Quiz Application",
            "email": "project.work81950@gmail.com"
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
                        .url("https://api.brevo.com/v3/smtp/email")
                        .addHeader("accept", "application/json")
                        .addHeader("api-key", apiKey)
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

            System.out.println(
                    "Brevo HTTP Status : "
                    + response.code());

            System.out.println(
                    "Brevo Response : "
                    + responseBody);

            System.out.println(
                    "================================");

            if (!response.isSuccessful()) {

                throw new RuntimeException(
                        "Brevo Error HTTP "
                        + response.code()
                        + ": "
                        + responseBody);
            }

        } catch (IOException e) {

            System.out.println(
                    "Brevo Connection Error: "
                    + e.getMessage());

            throw new RuntimeException(
                    "Unable to connect to Brevo",
                    e);
        }
    }

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}