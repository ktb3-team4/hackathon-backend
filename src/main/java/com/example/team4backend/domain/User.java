package com.example.team4backend.domain;

import com.example.team4backend.common.Role;
import com.example.team4backend.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Table(name = "users")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "refresh_token", length = 512)
    private String refreshToken;

    @Column(name = "is_onboarded", nullable = false)
    private boolean onboarded = false;

    private Instant deletedAt;


    @Builder
    public User(String email, String username, Role role) {
        this.email = email;
        this.username = username;
        this.role = role;
    }

    public void updateRefreshToken(String newRefreshToken) {
        if (newRefreshToken == null || newRefreshToken.isBlank()) {
            return;
        }
        this.refreshToken = newRefreshToken;
    }

    public void clearRefreshToken() {
        if(refreshToken != null) {
            refreshToken = null;
        }
    }

    public void completeOnboarding() {
        if (this.onboarded) {
            return;
        }
        this.onboarded = true;
    }

    public void delete() {
        if (this.deletedAt != null) {
            return;
        }
        this.deletedAt = Instant.now();
    }
}
