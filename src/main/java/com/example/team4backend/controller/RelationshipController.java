package com.example.team4backend.controller;

import com.example.team4backend.common.response.ApiResult;
import com.example.team4backend.dto.RelationshipResponse;
import com.example.team4backend.service.RelationshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Relationship", description = "관계 관리 API")
@RestController
@RequestMapping("/relationships")
@RequiredArgsConstructor
public class RelationshipController {

    private final RelationshipService relationshipService;

    @Operation(summary = "관계 목록 조회", description = "시스템에 등록된 모든 관계 유형을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResult<List<RelationshipResponse>>> getAllRelationships() {
        List<RelationshipResponse> relationships = relationshipService.getAllRelationships();
        return ResponseEntity.ok(ApiResult.ok(relationships));
    }
}
