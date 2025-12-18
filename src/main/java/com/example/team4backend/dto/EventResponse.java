package com.example.team4backend.dto;

import com.example.team4backend.domain.Event;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "이벤트 정보 응답")
public record EventResponse(
        @Schema(description = "이벤트 ID", example = "1")
        Long id,

        @Schema(description = "이벤트 날짜", example = "2025-01-10")
        LocalDate date,

        @Schema(description = "이벤트 설명", example = "결혼기념일")
        String description
) {
    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getDate(),
                event.getDescription()
        );
    }
}
