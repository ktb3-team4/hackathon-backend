package com.example.team4backend.dto;

import com.example.team4backend.domain.TargetPerson;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record TargetResponse(
        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "관계", example = "엄마")
        String relationName,

        @Schema(description = "채팅 스타일", example = "편한 반말")
        String chatStyleName,

        @Schema(description = "나이", example = "25")
        Integer age,

        @Schema(description = "전화번호", example = "01029050166")
        String phoneNumber,

        @Schema(description = "생일", example = "1999-12-17")
        LocalDate birthday,

        @Schema(description = "관심사 및 취미", example = "축구, 영화 감상, 맛집 탐방")
        String interests,

        @Schema(description = "이벤트 목록")
        List<EventResponse> events
){
    public static TargetResponse from(TargetPerson target) {
        return new TargetResponse(
                target.getName(),
                target.getRelationship().getDescription(),
                target.getChatStyle().getStyleName(),
                target.getAge(),
                target.getPhoneNumber(),
                target.getBirthday(),
                target.getInterests(),
                target.getEvents().stream()
                        .map(EventResponse::from)
                        .toList()
        );
    }
}
