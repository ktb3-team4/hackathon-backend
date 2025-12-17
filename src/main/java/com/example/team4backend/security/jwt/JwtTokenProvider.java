package com.example.team4backend.security.jwt;

import com.example.team4backend.common.error.ErrorCode;
import com.example.team4backend.domain.User;
import com.example.team4backend.dto.TokenResponse;
import com.example.team4backend.exception.BusinessException;
import com.github.f4b6a3.tsid.TsidCreator;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.access-exp-minutes}")
    private long accessExpMinutes;

    @Value("${app.jwt.refresh-exp-seconds}")
    private long refreshExpSeconds;

    // Key 객체
    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public TokenResponse createAccessToken(User user) {
        Instant now = Instant.now();
        Instant exp = now.plus(accessExpMinutes, ChronoUnit.MINUTES);

        String accessToken = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("username", user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public String createRefreshToken() {
        return TsidCreator.getTsid().toString();
    }

    // 서명 검증 + 파싱
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    public Long getUserIdFromAccessToken(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return Long.parseLong(claims.getSubject());
    }

    public String getEmailFromAccessToken(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.get("email", String.class);
    }

    public String getRoleFromAccessToken(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.get("role", String.class);
    }

    public String getUsernameFromAccessToken(String accessToken) {
        Claims claims = parseClaims(accessToken);
        return claims.get("username", String.class);
    }
}