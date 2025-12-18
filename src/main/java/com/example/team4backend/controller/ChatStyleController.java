package com.example.team4backend.controller;

import com.example.team4backend.common.response.ApiResult;
import com.example.team4backend.dto.ChatStyleResponse;
import com.example.team4backend.service.ChatStyleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ChatStyle", description = "채팅 스타일 관리 API")
@RestController
@RequestMapping("/chat-styles")
@RequiredArgsConstructor
public class ChatStyleController {

    private final ChatStyleService chatStyleService;

    @Operation(summary = "채팅 스타일 목록 조회", description = "시스템에 등록된 모든 채팅 스타일을 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResult<List<ChatStyleResponse>>> getAllChatStyles() {
        List<ChatStyleResponse> chatStyles = chatStyleService.getAllChatStyles();
        return ResponseEntity.ok(ApiResult.ok(chatStyles));
    }
}
