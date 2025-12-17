package com.example.team4backend.controller;

import com.example.team4backend.common.response.ApiResult;
import com.example.team4backend.dto.PromptRequest;
import com.example.team4backend.service.AiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ai", description = "LLM 호출 API")
@RestController
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/prompts")
    public ResponseEntity<ApiResult<Void>> chat(@RequestBody PromptRequest promptRequest) {
        System.out.println("=== AiController.chat() 호출됨 === userInput: " + promptRequest.getUserInput());
        String aiResponse = aiService.generateContent(promptRequest.getUserInput());
        System.out.println("=== AiController.chat() 응답 완료 ===");
        return ResponseEntity.ok(ApiResult.ok(aiResponse));
    }
}
