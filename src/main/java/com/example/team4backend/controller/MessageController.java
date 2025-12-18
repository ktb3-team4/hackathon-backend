package com.example.team4backend.controller;

import com.example.team4backend.common.response.ApiResult;
import com.example.team4backend.dto.MessageListResponse;
import com.example.team4backend.security.CustomUserDetails;
import com.example.team4backend.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Message", description = "메시지 관리 API")
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "메시지 리스트 조회", description = "대상자별 이름, 추천 서두, 마지막 연락 날짜를 리스트로 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResult<List<MessageListResponse>>> getMessageList(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<MessageListResponse> messageList = messageService.getMessageList(userDetails.getId());
        return ResponseEntity.ok(ApiResult.ok(messageList));
    }
}
