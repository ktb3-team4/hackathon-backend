package com.example.team4backend.service;


import com.example.team4backend.common.error.ErrorCode;
import com.example.team4backend.dto.KakaoTokenResponse;
import com.example.team4backend.dto.KakaoUserInfoResponse;
import com.example.team4backend.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoOAuthService {

    @Value("${oauth2.kakao.client-id}")
    private String clientId;

    @Value("${oauth2.kakao.client-secret:}")
    private String clientSecret;

    @Value("${oauth2.kakao.redirect-uri}")
    private String redirectUri;

    private final WebClient webClient = WebClient.create();

    /**
     * 카카오 인가 코드를 이용해 액세스 토큰을 요청합니다.
     */
    public KakaoTokenResponse getToken(String code) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);
        if (clientSecret != null && !clientSecret.isBlank()) {
            body.add("client_secret", clientSecret);
        }

        try {
            return webClient.post()
                    .uri(tokenUri)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(body))
                    .retrieve()
                    .bodyToMono(KakaoTokenResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to fetch Kakao token for code: {}", code, e);
            throw new BusinessException(ErrorCode.KAKAO_TOKEN_FETCH_FAILED);
        }
    }

    public KakaoUserInfoResponse getUserInfo(String kakaoAccessToken) {
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";

        try {
            return webClient.get()
                    .uri(userInfoUri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + kakaoAccessToken)
                    .retrieve()
                    .bodyToMono(KakaoUserInfoResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to fetch Kakao user info with token: {}", kakaoAccessToken, e);
            throw new BusinessException(ErrorCode.KAKAO_USER_INFO_FETCH_FAILED);
        }
    }
}
