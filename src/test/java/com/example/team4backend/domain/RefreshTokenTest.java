package com.example.team4backend.domain;

import com.example.team4backend.common.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Nested
@DisplayName("RefreshToken 관리")
class RefreshTokenTest {

    @Test
    @DisplayName("refreshToken이 정상 값이면 저장된다 (최초 발급)")
    void updateRefreshToken_initialIssue() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .username("tester")
                .role(Role.ROLE_USER)
                .build();

        // when
        user.updateRefreshToken("refresh-token");

        // then
        assertThat(user.getRefreshToken()).isEqualTo("refresh-token");
    }

    @Test
    @DisplayName("이미 refreshToken이 있어도 새로운 값으로 갱신된다")
    void updateRefreshToken_reissue() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .username("tester")
                .role(Role.ROLE_USER)
                .build();

        user.updateRefreshToken("old-token");

        // when
        user.updateRefreshToken("new-token");

        // then
        assertThat(user.getRefreshToken()).isEqualTo("new-token");
    }

    @Test
    @DisplayName("refreshToken이 null이면 무시된다")
    void updateRefreshToken_nullValue_ignored() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .username("tester")
                .role(Role.ROLE_USER)
                .build();

        // when
        user.updateRefreshToken(null);

        // then
        assertThat(user.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("refreshToken이 빈 문자열이면 무시된다")
    void updateRefreshToken_blank_ignored() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .username("tester")
                .role(Role.ROLE_USER)
                .build();

        // when
        user.updateRefreshToken("   ");

        // then
        assertThat(user.getRefreshToken()).isNull();
    }

    @Test
    @DisplayName("clearRefreshToken 호출 시 refreshToken은 null이 된다")
    void clearRefreshToken_success() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .username("tester")
                .role(Role.ROLE_USER)
                .build();

        user.updateRefreshToken("refresh-token");

        // when
        user.clearRefreshToken();

        // then
        assertThat(user.getRefreshToken()).isNull();
    }
}

