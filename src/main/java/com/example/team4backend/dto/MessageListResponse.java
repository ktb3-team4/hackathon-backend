package com.example.team4backend.dto;

import com.example.team4backend.domain.TargetPerson;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "메시지 리스트 응답")
public record MessageListResponse(
        @Schema(description = "대상자 ID", example = "1")
        Long targetId,

        @Schema(description = "대상자 이름", example = "홍길동")
        String name,

        @Schema(description = "전화번호", example = "01029050166")
        String phoneNumber,

        @Schema(description = "추천 서두", example = "안녕하세요! 오랜만이에요")
        String recommendedOpening,

        @Schema(description = "마지막 연락 날짜", example = "2024-01-15T10:30:00Z")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
        Instant lastMessageDate
) {
    public static MessageListResponse of(TargetPerson target, String recommendedOpening) {
        return new MessageListResponse(
                target.getId(),
                target.getName(),
                target.getPhoneNumber(),
                recommendedOpening,
                target.getLastMessageDate()
        );
    }
}
