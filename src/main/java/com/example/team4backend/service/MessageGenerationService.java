package com.example.team4backend.service;

import com.example.team4backend.common.error.ErrorCode;
import com.example.team4backend.dto.FastApiJobRequest;
import com.example.team4backend.dto.FastApiJobResponse;
import com.example.team4backend.exception.BusinessException;
import com.example.team4backend.repository.TargetPersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageGenerationService {

    private final FastApiClient fastApiClient;
    private final TargetPersonRepository targetPersonRepository;

    @Async
    @Transactional
    public void generateAndSaveMessage(Long targetId, FastApiJobRequest request) {
        log.info("Starting async message generation for targetId: {}", targetId);

        try {
            // 1. FastAPI에 Job 생성 요청
            // Mono를 동기식으로 대기 (결과가 올 때까지 대기, 별도의 스레드에서 실행)
            FastApiJobResponse jobResponse = fastApiClient.createJob(request).block();

            if (jobResponse == null || jobResponse.jobId() == null) {
                log.error("Failed to create job for targetId: {}", targetId);
                return;
            }

            String jobId = jobResponse.jobId();
            log.info("Job created with jobId: {} for targetId: {}", jobId, targetId);

            // 2. Job 완료될 때까지 폴링 (최대 30초)
            String result = pollJobResult(jobId, 30, 2);

            if (result != null) {
                // 3. 결과를 DB에 저장
                saveRecommendedOpening(targetId, result);
                log.info("Message successfully saved for targetId: {}", targetId);
            } else {
                log.warn("Job timeout or failed for targetId: {}", targetId);
            }

        } catch (Exception e) {
            log.error("Error during message generation for targetId: {}", targetId, e);
        }
    }

    // maxAttempts 대기
    private String pollJobResult(String jobId, int maxAttempts, int intervalSeconds) {
        for (int i = 0; i < maxAttempts; i++) {
            try {
                FastApiJobResponse statusResponse = fastApiClient.getJobStatus(jobId)
                        .block();

                if (statusResponse == null) {
                    log.warn("No response from FastAPI for jobId: {}", jobId);
                    continue;
                }

                String status = statusResponse.status();
                log.info("Job {} status: {}", jobId, status);

                if ("DONE".equals(status)) {
                    // 완료되면 결과 가져오기
                    FastApiJobResponse resultResponse = fastApiClient.getJobResult(jobId)
                            .block();

                    if (resultResponse != null && resultResponse.result() != null) {
                        return resultResponse.result();
                    }
                } else if ("ERROR".equals(status)) {
                    log.error("Job {} failed with error", jobId);
                    return null;
                }

                // 대기
                Thread.sleep(intervalSeconds * 1000L);

            } catch (InterruptedException e) {
                log.error("Polling interrupted for jobId: {}", jobId);
                Thread.currentThread().interrupt();
                return null;
            } catch (Exception e) {
                log.error("Error polling job status for jobId: {}", jobId, e);
            }
        }

        log.warn("Job {} did not complete within timeout", jobId);
        return null;
    }

    @Transactional
    public void saveRecommendedOpening(Long targetId, String message) {
        targetPersonRepository.findById(targetId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        targetPersonRepository.updateRecommendedOpening(targetId, message);
        log.info("Recommended opening saved for targetId: {}", targetId);
    }
}
