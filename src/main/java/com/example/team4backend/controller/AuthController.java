package com.example.team4backend.controller;


import com.example.team4backend.common.response.ApiResult;
import com.example.team4backend.dto.AuthTokens;
import com.example.team4backend.dto.TokenResponse;
import com.example.team4backend.common.util.CookieUtil;
import com.example.team4backend.security.CustomUserDetails;
import com.example.team4backend.service.AuthService;
import com.example.team4backend.exception.BusinessException;
import com.example.team4backend.common.error.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        AuthTokens tokens = authService.kakaoLogin(code);
        CookieUtil.addRefreshTokenCookie(response, tokens.refreshToken(), tokens.refreshTokenTtlSeconds());
        return ResponseEntity.ok(ApiResult.ok(tokens.accessToken()));
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 검증하고 새로운 액세스/리프레시 토큰을 발급합니다. 리프레시 토큰은 HttpOnly 쿠키로 재설정됩니다.")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResult<TokenResponse>> refresh(
            HttpServletRequest request,
            HttpServletResponse response,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }
        AuthTokens tokens = authService.refreshTokens(userDetails.getId(), CookieUtil.resolveRefreshToken(request));
        CookieUtil.addRefreshTokenCookie(response, tokens.refreshToken(), tokens.refreshTokenTtlSeconds());
        return ResponseEntity.ok(ApiResult.ok(tokens.accessToken()));
    }

    @Operation(summary = "로그아웃", description = "리프레시 토큰을 무효화하고 쿠키를 제거합니다.")
    @PostMapping("/logout")
    public ResponseEntity<ApiResult<Void>> logout(
            HttpServletResponse response,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (userDetails == null) {
            throw new BusinessException(ErrorCode.AUTH_UNAUTHORIZED);
        }
        authService.logout(userDetails.getId());
        CookieUtil.removeRefreshTokenCookie(response);
        return ResponseEntity.ok(ApiResult.ok());
    }
}
