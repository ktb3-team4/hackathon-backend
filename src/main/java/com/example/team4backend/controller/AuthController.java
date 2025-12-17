package com.example.team4backend.controller;


import com.example.team4backend.common.response.ApiResult;
import com.example.team4backend.dto.TokenResponse;
import com.example.team4backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증/인가 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "카카오 로그인", description = "카카오 인가 코드를 받아 로그인하고 JWT 토큰을 발급합니다. Refresh Token은 HttpOnly 쿠키로 설정됩니다.")
    @GetMapping("/kakao/login")
    public ResponseEntity<ApiResult<TokenResponse>> kakaoLogin(
            @RequestParam("code") String code,
            HttpServletResponse response
    ) {
        TokenResponse tokenResponse = authService.kakaoLogin(code, response);
        return ResponseEntity.ok(ApiResult.ok(tokenResponse));
    }
}