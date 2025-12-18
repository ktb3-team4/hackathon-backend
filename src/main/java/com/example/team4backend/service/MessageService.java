package com.example.team4backend.service;

import com.example.team4backend.domain.TargetPerson;
import com.example.team4backend.dto.MessageListResponse;
import com.example.team4backend.repository.TargetPersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final TargetPersonRepository targetPersonRepository;

    @Transactional(readOnly = true)
    public List<MessageListResponse> getMessageList(Long userId) {
        List<TargetPerson> targets = targetPersonRepository.findAllByUserIdWithDetailsAndDeletedAtIsNull(userId);

        return targets.stream()
                .map(target -> MessageListResponse.of(target, target.getRecommendedOpening()))
                .toList();
    }
}
