package com.example.team4backend.domain;


import com.example.team4backend.common.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Nested
    @DisplayName("User 생성")
    class CreateUser {

        @Test
        @DisplayName("Builder로 User를 생성하면 기본값이 올바르게 설정된다")
        void createUser_withBuilder() {
            // given
            String email = "test@example.com";
            String username = "tester";
            Role role = Role.ROLE_USER;

            // when
            User user = User.builder()
                    .email(email)
                    .username(username)
                    .role(role)
                    .build();

            // then
            assertThat(user.getId()).isNull();
            assertThat(user.getEmail()).isEqualTo(email);
            assertThat(user.getUsername()).isEqualTo(username);
            assertThat(user.getRole()).isEqualTo(role);
            assertThat(user.isOnboarded()).isFalse();
            assertThat(user.getRefreshToken()).isNull();
            assertThat(user.getDeletedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("온보딩 완료 처리")
    class OnboardingTest {

        @Test
        @DisplayName("온보딩을 완료하면 onboarded는 true가 된다")
        void completeOnboarding_success() {
            // given
            User user = User.builder()
                    .email("test@example.com")
                    .username("tester")
                    .role(Role.ROLE_USER)
                    .build();

            // when
            user.completeOnboarding();

            // then
            assertThat(user.isOnboarded()).isTrue();
        }

        @Test
        @DisplayName("이미 온보딩이 완료된 경우 다시 호출해도 상태는 변하지 않는다")
        void completeOnboarding_idempotent() {
            // given
            User user = User.builder()
                    .email("test@example.com")
                    .username("tester")
                    .role(Role.ROLE_USER)
                    .build();

            user.completeOnboarding();

            // when
            user.completeOnboarding();

            // then
            assertThat(user.isOnboarded()).isTrue();
        }
    }
}

