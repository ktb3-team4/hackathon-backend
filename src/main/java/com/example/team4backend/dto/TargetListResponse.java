package com.example.team4backend.dto;

import com.example.team4backend.domain.Relationship;
import com.example.team4backend.domain.TargetPerson;
import io.swagger.v3.oas.annotations.media.Schema;


public record TargetListResponse(
        @Schema(description = "이름", example = "홍길동")
        String name,

        @Schema(description = "관계 (FRIEND, FAMILY, COWORKER 등)", example = "FRIEND")
        Relationship relation
){
    public static TargetListResponse from(TargetPerson target) {
        return new TargetListResponse(
                target.getName(),
                target.getRelation()
        );
    }
}
