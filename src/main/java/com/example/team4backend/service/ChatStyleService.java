package com.example.team4backend.service;

import com.example.team4backend.dto.ChatStyleResponse;
import com.example.team4backend.repository.ChatStyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatStyleService {

    private final ChatStyleRepository chatStyleRepository;

    @Transactional(readOnly = true)
    public List<ChatStyleResponse> getAllChatStyles() {
        return chatStyleRepository.findAll()
                .stream()
                .map(ChatStyleResponse::from)
                .toList();
    }
}
