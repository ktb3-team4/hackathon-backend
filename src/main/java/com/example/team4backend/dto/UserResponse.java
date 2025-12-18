package com.example.team4backend.dto;

import com.example.team4backend.common.Role;
import com.example.team4backend.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "사용자 정보 응답")
public record UserResponse(
        @Schema(description = "이메일", example = "user@example.com")
        String email,

        @Schema(description = "닉네임", example = "홍길동")
        String username,

        @Schema(description = "역할", example = "ROLE_USER")
        Role role,

        @Schema(description = "온보딩 완료 여부", example = "true")
        boolean onboarded
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getEmail(),
                user.getUsername(),
                user.getRole(),
                user.isOnboarded()
        );
    }
}
