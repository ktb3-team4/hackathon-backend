package com.example.team4backend.exception;

import com.example.team4backend.common.error.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Schema(name = "ErrorResponseDto", description = "공통 에러 래퍼")
public class ErrorResponseDto {
    @Schema(description = "결과 코드", example = "USER_NOT_FOUND")
    private final String code;

    @Schema(description = "메시지", example = "사용자를 찾을 수 없습니다.")
    private final String message;

    @Schema(description = "필드 단위 오류", nullable = true)
    private final List<FieldErrorDetail> errors;

    public static ErrorResponseDto from(CustomException e) {
        return new ErrorResponseDto(e.getErrorCode().getCode(), e.getMessage(), null);
    }

    public static ErrorResponseDto of(String code, String message) {
        return new ErrorResponseDto(code, message, null);
    }

    public static ErrorResponseDto of(ErrorCode errorCode) {
        return new ErrorResponseDto(errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static ErrorResponseDto of(ErrorCode errorCode, List<FieldErrorDetail> fieldErrors) {
        return new ErrorResponseDto(errorCode.getCode(), errorCode.getMessage(), fieldErrors);
    }

    @Getter
    @AllArgsConstructor
    public static class FieldErrorDetail {
        @Schema(description = "필드명", example = "email")
        private final String field;

        @Schema(description = "거부된 값", example = "not-an-email")
        private final Object rejectedValue;

        @Schema(description = "실패 사유", example = "이메일 형식이 아닙니다.")
        private final String reason;
    }
}
