package com.example.team4backend.exception;

import com.example.team4backend.common.error.ErrorCode;

public class BusinessException extends CustomException {
    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }
}

