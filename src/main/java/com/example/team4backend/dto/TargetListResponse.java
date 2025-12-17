package com.example.team4backend.dto;

import com.example.team4backend.domain.TargetPerson;
import io.swagger.v3.oas.annotations.media.Schema;


public record TargetListResponse(
        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "관계", example = "엄마")
        String relationName
){
    public static TargetListResponse from(TargetPerson target) {
        return new TargetListResponse(
                target.getName(),
                target.getRelationship().getDescription()
        );
    }
}
