package com.example.team4backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "PromptRequest", description = "프롬프트 요청")
public class PromptRequest {
    String userInput;
}
