package com.example.team4backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.time.Instant;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserInfoResponse {
    private Long id;
    private Boolean hasSignedUp;
    private Instant connectedAt;
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KakaoAccount {
        private Boolean profileNicknameNeedsAgreement;
        private Boolean profileImageNeedsAgreement;
        private Profile profile;
        private Boolean hasEmail;
        private Boolean emailNeedsAgreement;
        private String email;

        @Getter
        public static class Profile {
            private String nickname;
            @JsonProperty("thumbnail_image_url")
            private String thumbnailUrl;
            @JsonProperty("profile_image_url")
            private String profileUrl;
            @JsonProperty("is_default_image")
            private Boolean isDefaultImage;
        }
    }

    // 이메일만 추출하는 헬퍼 메서드
    public String getEmail() {
        if (kakaoAccount == null) return null;
        return kakaoAccount.getEmail();
    }

    // [추가] 닉네임만 추출하는 헬퍼 메서드
    public String getNickname() {
        if (kakaoAccount == null || kakaoAccount.getProfile() == null) return null;
        return kakaoAccount.getProfile().getNickname();
    }
}
