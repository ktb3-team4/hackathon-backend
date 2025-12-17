package com.example.team4backend.service;

import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.chat.prompt.Prompt;

@Service
public class OpenAiService implements AiService {

    private final OpenAiChatModel chatModel;

    @Value("${spring.ai.openai.chat.options.model}")
    private String modelName;

    public OpenAiService(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public String generateContent(String prompt) {

        var options = (OpenAiChatOptions) getOptions();

        return chatModel.call(new Prompt(prompt, options)).getResult()
                .getOutput().getText();
    }

    public Object getOptions() {
        // TODO: relationship table에서 weight 출력

        return OpenAiChatOptions.builder()
                .model(modelName)
                .temperature(0.5)
                .maxTokens(150)
                .build();
    }
}
