package com.example.team4backend.service;

import com.example.team4backend.common.Role;
import com.example.team4backend.common.error.ErrorCode;
import com.example.team4backend.domain.User;
import com.example.team4backend.dto.AuthTokens;
import com.example.team4backend.dto.KakaoUserInfoResponse;
import com.example.team4backend.dto.TokenResponse;
import com.example.team4backend.exception.BusinessException;
import com.example.team4backend.repository.UserRepository;
import com.example.team4backend.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoOAuthService kakaoOAuthService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthTokens kakaoLogin(String code) {
        String kakaoAccessToken = kakaoOAuthService.getToken(code).getAccessToken();

        KakaoUserInfoResponse userInfo = kakaoOAuthService.getUserInfo(kakaoAccessToken);
        String email = userInfo.getEmail();
        String nickname = userInfo.getNickname();

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseGet(() -> signUp(email, nickname));

        TokenResponse tokenResponse = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken(user);

        user.updateRefreshToken(refreshToken);

        return new AuthTokens(tokenResponse, refreshToken, jwtTokenProvider.getRefreshExpSeconds());
    }

    @Transactional
    public AuthTokens refreshTokens(Long userId, String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BusinessException(ErrorCode.NOT_EXIST_REFRESH_TOKEN);
        }

        Claims claims = jwtTokenProvider.validateRefreshToken(refreshToken);
        Long subjectUserId = Long.parseLong(claims.getSubject());

        if (!subjectUserId.equals(userId)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (!refreshToken.equals(user.getRefreshToken())) {
            user.clearRefreshToken();
            throw new BusinessException(ErrorCode.INVALID_TOKEN_REUSE_DETECTED);
        }

        TokenResponse tokenResponse = jwtTokenProvider.createAccessToken(user);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(user);

        user.updateRefreshToken(newRefreshToken);

        return new AuthTokens(tokenResponse, newRefreshToken, jwtTokenProvider.getRefreshExpSeconds());
    }

    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        user.clearRefreshToken();
    }

    private User signUp(String email, String nickname) {
        User user = User.builder()
                .email(email)
                .username(nickname)
                .role(Role.ROLE_USER)
                .build();
        return userRepository.save(user);
    }
}
