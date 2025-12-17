package com.example.team4backend.security;

import com.example.team4backend.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;

    private CustomUserDetails(
            Long id,
            String email,
            String username,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.authorities = authorities;
    }

    public static CustomUserDetails from(User user) {
        String roleName = user.getRole().name();
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(roleName));

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                authorities
        );
    }

    public static CustomUserDetails fromClaims(Long id, String email, String username, String roleName) {
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(roleName));

        return new CustomUserDetails(id, email, username, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return username;
    }
}
