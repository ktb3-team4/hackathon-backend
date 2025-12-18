package com.example.team4backend.service;

import com.example.team4backend.common.error.ErrorCode;
import com.example.team4backend.domain.ChatStyle;
import com.example.team4backend.domain.Event;
import com.example.team4backend.domain.Relationship;
import com.example.team4backend.domain.TargetPerson;
import com.example.team4backend.domain.User;
import com.example.team4backend.dto.TargetListResponse;
import com.example.team4backend.dto.TargetRequest;
import com.example.team4backend.dto.TargetResponse;
import com.example.team4backend.exception.BusinessException;
import com.example.team4backend.repository.ChatStyleRepository;
import com.example.team4backend.repository.EventRepository;
import com.example.team4backend.repository.RelationshipRepository;
import com.example.team4backend.repository.TargetPersonRepository;
import com.example.team4backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TargetService {

    private final TargetPersonRepository targetPersonRepository;
    private final UserRepository userRepository;
    private final RelationshipRepository relationshipRepository;
    private final ChatStyleRepository chatStyleRepository;
    private final EventRepository eventRepository;

    @Transactional
    public Long addTarget(Long userId, TargetRequest dto) {
        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Relationship relationship = relationshipRepository.findById(dto.relationshipId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        ChatStyle chatStyle = chatStyleRepository.findById(dto.chatStyleId())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        TargetPerson target = TargetPerson.builder()
                .user(user)
                .name(dto.name())
                .relationship(relationship)
                .chatStyle(chatStyle)
                .chatContent(dto.chatContent())
                .age(dto.age())
                .phoneNumber(dto.phoneNumber())
                .birthday(dto.birthday())
                .interests(dto.interests())
                .build();

        TargetPerson savedTarget = targetPersonRepository.save(target);

        // 이벤트 저장
        if (dto.events() != null && !dto.events().isEmpty()) {
            dto.events().forEach(eventDto -> {
                Event event = Event.builder()
                        .targetPerson(savedTarget)
                        .date(eventDto.date())
                        .description(eventDto.description())
                        .build();
                savedTarget.addEvent(event);
            });
        }

        if (!user.isOnboarded()) {
            user.completeOnboarding();
        }

        return savedTarget.getId();
    }

    @Transactional(readOnly = true)
    public TargetResponse getTarget(Long userId, Long targetId) {
        TargetPerson target = targetPersonRepository.findByIdWithUserAndDetailsAndDeletedAtIsNull(targetId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        if (!target.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        }
        return TargetResponse.from(target);
    }

    @Transactional(readOnly = true)
    public List<TargetListResponse> getAllTargets(Long userId) {
        List<TargetPerson> targets = targetPersonRepository.findAllByUserIdWithDetailsAndDeletedAtIsNull(userId);

        return targets.stream()
                .map(TargetListResponse::from)
                .toList();
    }

    @Transactional
    public void updateTarget(Long userId, Long targetId, TargetRequest dto) {
        TargetPerson target = targetPersonRepository.findByIdWithUserAndDetailsAndDeletedAtIsNull(targetId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        if (!target.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.AUTH_FORBIDDEN);
        }

        Relationship relationship = target.getRelationship();
        if (!relationship.getId().equals(dto.relationshipId())) {
            relationship = relationshipRepository.findById(dto.relationshipId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        }

        ChatStyle chatStyle = target.getChatStyle();
        if (!chatStyle.getId().equals(dto.chatStyleId())) {
            chatStyle = chatStyleRepository.findById(dto.chatStyleId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        }

        target.update(
                dto.name(),
                relationship,
                chatStyle,
                dto.age(),
                dto.phoneNumber(),
                dto.birthday(),
                dto.interests()
        );

        // 기존 이벤트 삭제 후 새로 추가
        target.clearEvents();
        if (dto.events() != null && !dto.events().isEmpty()) {
            dto.events().forEach(eventDto -> {
                Event event = Event.builder()
                        .targetPerson(target)
                        .date(eventDto.date())
                        .description(eventDto.description())
                        .build();
                target.addEvent(event);
            });
        }
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

    @Transactional
    public void updateLastMessageDate(Long userId, Long targetId) {
        int updated = targetPersonRepository.updateLastMessageDate(
                targetId,
                userId,
                Instant.now()
        );

        if (updated == 0) {
            // targetId가 존재하지 않거나 userId가 일치하지 않음
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
    }
}
