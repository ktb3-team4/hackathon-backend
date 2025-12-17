package com.example.team4backend.dto;

import com.example.team4backend.domain.Relationship;
import com.example.team4backend.domain.TargetPerson;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record TargetResponse(
        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "관계 (FRIEND, FAMILY, COWORKER 등)", example = "FRIEND")
        Relationship relation,

        @Schema(description = "나이", example = "25")
        Integer age,

        @Schema(description = "전화번호", example = "01029050166")
        String phoneNumber,

        @Schema(description = "생일", example = "1999-12-17")
        LocalDate birthday,

        @Schema(description = "직업", example = "개발자")
        String job,

        @Schema(description = "관심사 및 취미", example = "축구, 영화 감상, 맛집 탐방")
        String interests,

        @Schema(description = "최근 이벤트", example = "최근에 새로운 프로젝트를 시작함")
        String events,

        @Schema(description = "기타 메모", example = "말투가 다정하고 리액션이 좋음")
        String memo
){
    public static TargetResponse from(TargetPerson target) {
        return new TargetResponse(
                target.getName(),
                target.getRelation(),
                target.getAge(),
                target.getPhoneNumber(),
                target.getBirthday(),
                target.getJob(),
                target.getInterests(),
                target.getEvents(),
                target.getMemo()
        );
    }
}
