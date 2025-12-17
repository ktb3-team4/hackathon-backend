package com.example.team4backend.service;

import com.example.team4backend.common.Role;
import com.example.team4backend.common.util.CookieUtil;
import com.example.team4backend.domain.User;
import com.example.team4backend.dto.KakaoUserInfoResponse;
import com.example.team4backend.dto.TokenResponse;
import com.example.team4backend.repository.UserRepository;
import com.example.team4backend.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
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
    public TokenResponse kakaoLogin(String code, HttpServletResponse response) {
        String kakaoAccessToken = kakaoOAuthService.getToken(code).getAccessToken();

        KakaoUserInfoResponse userInfo = kakaoOAuthService.getUserInfo(kakaoAccessToken);
        String email = userInfo.getEmail();
        String nickname = userInfo.getNickname();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> signUp(email, nickname));

        TokenResponse tokenResponse = jwtTokenProvider.createAccessToken(user);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        user.updateRefreshToken(refreshToken);
        CookieUtil.addRefreshTokenCookie(response, refreshToken);

        return tokenResponse;
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
