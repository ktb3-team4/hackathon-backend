package com.example.team4backend.service;

import com.example.team4backend.common.error.ErrorCode;
import com.example.team4backend.dto.UserResponse;
import com.example.team4backend.exception.BusinessException;
import com.example.team4backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getUserInfo(Long userId) {
        return userRepository.findByIdAndDeletedAtIsNull(userId)
                .map(UserResponse::from)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
