package com.example.team4backend.service;

import com.example.team4backend.dto.RelationshipResponse;
import com.example.team4backend.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelationshipService {

    private final RelationshipRepository relationshipRepository;

    @Transactional(readOnly = true)
    public List<RelationshipResponse> getAllRelationships() {
        return relationshipRepository.findAll()
                .stream()
                .map(RelationshipResponse::from)
                .toList();
    }
}
