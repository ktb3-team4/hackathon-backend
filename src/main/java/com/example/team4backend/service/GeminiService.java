package com.example.team4backend.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService implements AiService {

    private final Client client;

    private static final String MODEL = "gemini-2.5-flash";

    public GeminiService(@Value("${spring.ai.google.genai.api-key}") String apiKey) {
        this.client = com.google.genai.Client.builder()
                .apiKey(apiKey)
                .build();
    }


    public String generateContent(String prompt) {
        GenerateContentResponse response = client.models.generateContent(
                MODEL,
                prompt,
                null);

        return response.text();
    }

}
