package com.example.team4backend.domain;

import com.example.team4backend.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_person_id", nullable = false)
    private TargetPerson targetPerson;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Builder
    public Event(TargetPerson targetPerson, LocalDate date, String description) {
        this.targetPerson = targetPerson;
        this.date = date;
        this.description = description;
    }

    public void update(LocalDate date, String description) {
        this.date = date;
        this.description = description;
    }
}
