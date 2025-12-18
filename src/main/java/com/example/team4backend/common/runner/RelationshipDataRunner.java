package com.example.team4backend.common.runner;

import com.example.team4backend.domain.Relationship;
import com.example.team4backend.repository.RelationshipRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RelationshipDataRunner implements CommandLineRunner {

    private final RelationshipRepository relationshipRepository;

    public RelationshipDataRunner(RelationshipRepository relationshipRepository) {
        this.relationshipRepository = relationshipRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 이미 데이터가 있으면 스킵
        if (relationshipRepository.count() > 0) {
            return;
        }

        // 초기 데이터 삽입
        List<Relationship> relationships = Arrays.asList(
                new Relationship("FATHER", "아빠"),
                new Relationship("MOTHER", "엄마"),
                new Relationship("GRANDFATHER", "할아버지"),
                new Relationship("GRANDMOTHER", "할머니"),
                new Relationship("OTHER", "기타")
        );

        relationshipRepository.saveAll(relationships);

        System.out.println("Relationship 초기 데이터 " + relationships.size() + "개 삽입 완료");
    }
}
