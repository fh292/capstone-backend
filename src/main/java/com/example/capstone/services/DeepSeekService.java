package com.example.capstone.services;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class DeepSeekService {
    private final String DEEPSEEK_API_URL = "https://api.deepseek.com/v1/chat/completions";
    private final String API_KEY = "my-deepseek-api-key"; // Replace with your key

    public String getSavingsSuggestion(Map<String, Object> insights) {
        RestTemplate restTemplate = new RestTemplate();

        String prompt = "Here is a user's spending pattern: " + insights.toString() +
                ". Based on this data, suggest ways they can save money.";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + API_KEY);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(DEEPSEEK_API_URL, HttpMethod.POST, entity, Map.class);

        return response.getBody().get("choices").toString();
    }
}
