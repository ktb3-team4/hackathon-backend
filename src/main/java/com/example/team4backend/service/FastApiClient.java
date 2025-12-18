package com.example.team4backend.service;

import com.example.team4backend.dto.FastApiJobRequest;
import com.example.team4backend.dto.FastApiJobResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FastApiClient {

    private final WebClient webClient;

    @Value("${fastapi.base-url:http://localhost:8000}")
    private String fastApiBaseUrl;

    public Mono<FastApiJobResponse> createJob(FastApiJobRequest request) {
        log.info("Creating FastAPI job for: {}", request.name());

        return webClient.post()
                .uri(fastApiBaseUrl + "/fastapi/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(FastApiJobResponse.class)
                .doOnSuccess(response -> log.info("Job created successfully: {}", response.jobId()))
                .doOnError(error -> log.error("Failed to create job", error));
    }

    public Mono<FastApiJobResponse> getJobResult(String jobId) {
        log.info("Fetching job result for: {}", jobId);

        return webClient.get()
                .uri(fastApiBaseUrl + "/fastapi/jobs/" + jobId + "/result")
                .retrieve()
                .bodyToMono(FastApiJobResponse.class)
                .doOnSuccess(response -> log.info("Job result fetched: {} - status: {}", jobId, response.status()))
                .doOnError(error -> log.error("Failed to fetch job result for: {}", jobId, error));
    }

    public Mono<FastApiJobResponse> getJobStatus(String jobId) {
        log.info("Fetching job status for: {}", jobId);

        return webClient.get()
                .uri(fastApiBaseUrl + "/fastapi/jobs/" + jobId)
                .retrieve()
                .bodyToMono(FastApiJobResponse.class)
                .doOnSuccess(response -> log.info("Job status fetched: {} - status: {}", jobId, response.status()))
                .doOnError(error -> log.error("Failed to fetch job status for: {}", jobId, error));
    }
}
