package com.example.team4backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FastApiJobResponse(
        @JsonProperty("job_id")
        String jobId,

        String status,

        String result
) {
}
