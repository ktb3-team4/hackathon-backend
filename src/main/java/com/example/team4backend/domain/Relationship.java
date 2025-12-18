package com.example.team4backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "relationships")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String description;

    public Relationship(String code, String description) {
        this.code = code;
        this.description = description;
    }
}