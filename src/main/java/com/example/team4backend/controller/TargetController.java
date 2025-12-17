package com.example.team4backend.controller;

import com.example.team4backend.common.response.ApiResult;
import com.example.team4backend.dto.TargetListResponse;
import com.example.team4backend.dto.TargetRequest;
import com.example.team4backend.dto.TargetResponse;
import com.example.team4backend.security.CustomUserDetails;
import com.example.team4backend.service.TargetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Target", description = "연락 대상자 관리 API")
@RestController
@RequestMapping("/targets")
@RequiredArgsConstructor
public class TargetController {

    private final TargetService targetService;

    @Operation(summary = "대상자 등록", description = "연락할 상대방의 정보를 등록합니다. 첫 등록 시 사용자의 온보딩 상태가 완료로 변경됩니다.")
    @PostMapping
    public ResponseEntity<ApiResult<Long>> addTarget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody TargetRequest requestDto
    ) {
        Long targetId = targetService.addTarget(userDetails.getId(), requestDto);
        return ResponseEntity.ok(ApiResult.ok("대상자가 성공적으로 등록되었습니다.", targetId));
    }

    @Operation(summary = "대상자 상세 조회", description = "특정 대상자의 상세 정보를 조회합니다.")
    @GetMapping("/{targetId}")
    public ResponseEntity<ApiResult<TargetResponse>> getTarget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long targetId
    ) {
        TargetResponse target = targetService.getTarget(userDetails.getId(), targetId);
        return ResponseEntity.ok(ApiResult.ok(target));
    }

    @Operation(summary = "대상자 정보 수정", description = "등록된 상대방의 정보를 수정합니다.")
    @PutMapping("/{targetId}")
    public ResponseEntity<ApiResult<Void>> updateTarget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long targetId,
            @Valid @RequestBody TargetRequest requestDto
    ) {
        targetService.updateTarget(userDetails.getId(), targetId, requestDto);
        return ResponseEntity.ok(ApiResult.ok("대상자 정보가 수정되었습니다."));
    }

    @Operation(summary = "대상자 목록 조회", description = "내가 등록한 모든 대상자의 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResult<List<TargetListResponse>>> getAllTargets(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<TargetListResponse> targets = targetService.getAllTargets(userDetails.getId());
        return ResponseEntity.ok(ApiResult.ok(targets));
    }

    @Operation(summary = "대상자 삭제", description = "등록된 상대방 정보를 소프트 딜리트 방식으로 삭제합니다.")
    @DeleteMapping("/{targetId}")
    public ResponseEntity<ApiResult<Void>> deleteTarget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long targetId
    ) {
        targetService.deleteTarget(userDetails.getId(), targetId);
        return ResponseEntity.ok(ApiResult.ok("대상자 정보가 삭제되었습니다."));
    }
}