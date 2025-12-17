package com.example.team4backend.controller;

import com.example.team4backend.common.error.ErrorCode;
import com.example.team4backend.common.response.ApiResult;
import com.example.team4backend.dto.UserResponse;
import com.example.team4backend.exception.BusinessException;
import com.example.team4backend.security.CustomUserDetails;
import com.example.team4backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "사용자 정보 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "인증된 사용자의 프로필 정보를 반환합니다.")
    @GetMapping("/me")
    public ResponseEntity<ApiResult<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }
        UserResponse response = userService.getUserInfo(userDetails.getId());
        return ResponseEntity.ok(ApiResult.ok(response));
    }
}
