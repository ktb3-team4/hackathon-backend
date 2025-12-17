package com.example.team4backend.service;

import com.example.team4backend.common.error.ErrorCode;
import com.example.team4backend.domain.TargetPerson;
import com.example.team4backend.domain.User;
import com.example.team4backend.dto.TargetListResponse;
import com.example.team4backend.dto.TargetRequest;
import com.example.team4backend.dto.TargetResponse;
import com.example.team4backend.exception.BusinessException;
import com.example.team4backend.repository.TargetPersonRepository;
import com.example.team4backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TargetService {

    private final TargetPersonRepository targetPersonRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long addTarget(Long userId, TargetRequest dto) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        TargetPerson target = TargetPerson.builder()
                .user(user)
                .name(dto.name())
                .relation(dto.relation())
                .age(dto.age())
                .phoneNumber(dto.phoneNumber())
                .birthday(dto.birthday())
                .job(dto.job())
                .interests(dto.interests())
                .events(dto.events())
                .memo(dto.memo())
                .build();

        if (!user.isOnboarded()) {
            user.completeOnboarding();
        }
        return targetPersonRepository.save(target).getId();
    }

    @Transactional(readOnly = true)
    public TargetResponse getTarget(Long userId, Long targetId) {
        TargetPerson target = targetPersonRepository.findByIdWithUserAndDeletedAtIsNull(targetId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        if (!target.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        }
        return TargetResponse.from(target);
    }

    @Transactional(readOnly = true)
    public List<TargetListResponse> getAllTargets(Long userId) {
        List<TargetPerson> targets = targetPersonRepository.findAllByUserIdWithUserAndDeletedAtIsNull(userId);

        return targets.stream()
                .map(TargetListResponse::from)
                .toList();
    }

    @Transactional
    public void updateTarget(Long userId, Long targetId, TargetRequest dto) {
        TargetPerson target = targetPersonRepository.findByIdWithUserAndDeletedAtIsNull(targetId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        if (!target.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        }

        target.update(dto.name(), dto.relation(), dto.age(), dto.phoneNumber(), dto.birthday(),
                dto.job(), dto.interests(), dto.events(), dto.memo());
    }

    @Transactional
    public void deleteTarget(Long userId, Long targetId) {
        int updated = targetPersonRepository.softDeleteByIdAndUserId(targetId, userId);
        if (updated > 0) {
            return;
        }

        targetPersonRepository.findOwnerIdByIdAndDeletedAtIsNull(targetId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
    }
}
