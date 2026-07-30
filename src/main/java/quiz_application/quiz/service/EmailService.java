package quiz_application.quiz.service;

import java.io.IOException;

import okhttp3.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient();

    public void sendEmail(String to,
                          String subject,
                          String body) {

        String json = """
        {
          "sender":{
            "name":"Quiz Application",
            "email":"project.work81950@gmail.com"
          },
          "to":[
            {
              "email":"%s"
            }
          ],
          "subject":"%s",
          "textContent":"%s"
        }
        """.formatted(
                to,
                subject.replace("\"","\\\""),
                body.replace("\"","\\\"")
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
                        .addHeader("content-type", "application/json")
                        .post(requestBody)
                        .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {

                String error =
                        response.body() != null
                                ? response.body().string()
                                : "Unknown error";

                throw new RuntimeException(
                        "Brevo Error: " + error);
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to send email",
                    e);
        }
    }
}