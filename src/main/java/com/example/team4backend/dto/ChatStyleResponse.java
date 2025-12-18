package com.example.team4backend.dto;

import com.example.team4backend.domain.ChatStyle;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "채팅 스타일 정보 응답")
public record ChatStyleResponse(
        @Schema(description = "채팅 스타일 ID", example = "1")
        Long id,

        @Schema(description = "스타일 이름", example = "편한 반말")
        String styleName,

        @Schema(description = "스타일 설명", example = "친근하고 편안한 반말 스타일")
        String description
) {
    public static ChatStyleResponse from(ChatStyle chatStyle) {
        return new ChatStyleResponse(
                chatStyle.getId(),
                chatStyle.getStyleName(),
                chatStyle.getDescription()
        );
    }
}
