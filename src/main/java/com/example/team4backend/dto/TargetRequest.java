package com.example.team4backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "상대방 정보 등록 요청")
public record TargetRequest(
        @Schema(description = "이름", example = "홍길동")
        @NotBlank(message = "이름은 필수입니다.")
        String name,

        @Schema(description = "관계 ID", example = "1")
        @NotNull(message = "관계는 필수입니다.")
        Long relationshipId,

        @Schema(description = "채팅 스타일 ID", example = "1")
        @NotNull(message = "채팅 스타일은 필수입니다.")
        Long chatStyleId,

        @Schema(description = "나이", example = "25")
        Integer age,

        @Schema(description = "전화번호", example = "01029050166")
        String phoneNumber,

        @Schema(description = "생일", example = "1999-12-17")
        LocalDate birthday,

        @Schema(description = "관심사 및 취미", example = "축구, 영화 감상, 맛집 탐방")
        String interests,

        @Schema(description = "이벤트 목록")
        @Valid
        List<EventRequest> events
) {
}