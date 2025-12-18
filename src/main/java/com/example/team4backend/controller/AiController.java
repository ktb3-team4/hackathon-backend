package com.example.team4backend.controller;

import com.example.team4backend.common.response.ApiResult;
import com.example.team4backend.dto.PromptRequest;
import com.example.team4backend.service.AiService;
import com.example.team4backend.service.ImageInput;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Ai", description = "LLM 호출 API")
@RestController
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/prompts")
    public ResponseEntity<ApiResult<Void>> chat(@RequestBody PromptRequest promptRequest) {
        String aiResponse = aiService.generateContent(promptRequest.getUserInput());

        return ResponseEntity.ok(ApiResult.ok(aiResponse));
    }

    @PostMapping(value = "/prompts/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResult<Void>> chatWithImages(
            @RequestPart("images") List<MultipartFile> images) {

        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("최소 1개 이상의 이미지가 필요합니다.");
        }
        if (images.size() > 3) {
            throw new IllegalArgumentException("이미지는 최대 3개까지 업로드할 수 있습니다.");
        }

        List<ImageInput> imageInputs = images.stream()
                .map(file -> {
                    try {
                        String mimeType = file.getContentType() != null
                                ? file.getContentType()
                                : MediaType.APPLICATION_OCTET_STREAM_VALUE;
                        return new ImageInput(file.getBytes(), mimeType);
                    } catch (IOException e) {
                        throw new RuntimeException("이미지 파일을 읽는 중 오류가 발생했습니다.", e);
                    }
                })
                .toList();

        String aiResponse = aiService.generateContentFromImages(imageInputs);
        return ResponseEntity.ok(ApiResult.ok(aiResponse));
    }
}
