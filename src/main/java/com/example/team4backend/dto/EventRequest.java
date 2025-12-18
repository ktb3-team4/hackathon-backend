package com.example.team4backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "이벤트 정보 요청")
public record EventRequest(
        @Schema(description = "이벤트 날짜", example = "2025-01-10")
        @NotNull(message = "이벤트 날짜는 필수입니다.")
        LocalDate date,

        @Schema(description = "이벤트 설명", example = "결혼기념일")
        @NotBlank(message = "이벤트 설명은 필수입니다.")
        String description
) {
}
