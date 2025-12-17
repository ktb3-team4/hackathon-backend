package com.example.team4backend.controller;

import com.example.team4backend.common.response.ApiResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public class UserController {
    @GetMapping
    public ResponseEntity<ApiResult<Void>> ok()
    {
        return ResponseEntity.ok(ApiResult.ok());
    }
}
