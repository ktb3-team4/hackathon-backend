package com.example.team4backend.dto;

import com.example.team4backend.domain.Relationship;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "관계 정보 응답")
public record RelationshipResponse(
        @Schema(description = "관계 ID", example = "1")
        Long id,

        @Schema(description = "관계 코드", example = "FRIEND")
        String code,

        @Schema(description = "관계 설명", example = "친구")
        String description
) {
    public static RelationshipResponse from(Relationship relationship) {
        return new RelationshipResponse(
                relationship.getId(),
                relationship.getCode(),
                relationship.getDescription()
        );
    }
}
