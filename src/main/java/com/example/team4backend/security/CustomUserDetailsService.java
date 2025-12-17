package com.example.team4backend.security;

import com.example.team4backend.common.error.ErrorCode;
import com.example.team4backend.exception.BusinessException;
import com.example.team4backend.exception.UserNotFoundException;
import com.example.team4backend.repository.UserRepository;
import com.example.team4backend.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 인증 과정에서 이 메서드를 호출해서 UserDetails를 로드함
    // username 파라미터에는 JWT에서 추출한 userId가 문자열로 들어옴
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        long userId;
        try {
            userId = Long.parseLong(username);
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.INVALID_USER_ID);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return CustomUserDetails.from(user);
    }
}


