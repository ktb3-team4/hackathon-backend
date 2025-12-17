package com.example.team4backend.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;

//@Service
public class GeminiService implements AiService {

    private final Client client;

    private static final String MODEL = "gemini-1.5-flash";

    public GeminiService(@Value("${spring.ai.google.genai.api-key}") String apiKey) {
        this.client = com.google.genai.Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public String generateContent(String prompt) {
        int maxRetries = 2;
        int retryDelay = 2000; // 2초

        for (int i = 0; i < maxRetries; i++) {
            try {
                GenerateContentResponse response = client.models.generateContent(
                        MODEL,
                        prompt,
                        null);
                return response.text();
            } catch (Exception e) {
                System.out.println("Gemini API 호출 실패 (시도 " + (i + 1) + "/" + maxRetries + "): " + e.getMessage());
                if (i < maxRetries - 1) {
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw e;
                }
            }
        }
        throw new RuntimeException("Gemini API 호출 실패");
    }

    @Override
    public Object getOptions() {
        return null;
    }
}
