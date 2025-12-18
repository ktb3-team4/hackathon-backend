package com.example.team4backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FastApiJobRequest(
        String name,
        String relationName,
        String chatStyleName,
        Integer age,
        String birthday,
        String lastContactDate,
        String interests,
        List<FastApiEventDto> events
) {
    public static FastApiJobRequest from(String name, String relationName, String chatStyleName,
                                          Integer age, LocalDate birthday,
                                          LocalDate lastContactDate, String interests,
                                          List<EventRequest> events) {
        return FastApiJobRequest.builder()
                .name(name)
                .relationName(relationName)
                .chatStyleName(chatStyleName)
                .age(age)
                .birthday(birthday != null ? birthday.toString() : null)
                .lastContactDate(lastContactDate != null ? lastContactDate.toString() : null)
                .interests(interests)
                .events(events != null ? events.stream()
                        .map(e -> new FastApiEventDto(
                                e.date() != null ? e.date().toString() : null,
                                e.description()))
                        .toList() : null)
                .build();
    }

    public record FastApiEventDto(
            String date,
            String description
    ) {}
}
