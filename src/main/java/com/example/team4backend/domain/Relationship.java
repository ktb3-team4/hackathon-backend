package com.example.team4backend.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Relationship {
    FRIEND("친구"),
    FAMILY("가족"),
    COWORKER("직장동료"),
    OTHER("기타");

    private final String description;
}
