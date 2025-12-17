package com.example.team4backend.repository;

import com.example.team4backend.domain.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
}
