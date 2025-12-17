package com.example.team4backend.domain;

import com.example.team4backend.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "target_people")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TargetPerson extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Relationship relation;

    private Integer age;

    @Column(nullable = false)
    private String phoneNumber;

    private LocalDate birthday;
    private String job;

    @Column(columnDefinition = "TEXT")
    private String interests;

    @Column(columnDefinition = "TEXT")
    private String events;

    @Column(columnDefinition = "TEXT")
    private String memo;

    private Instant deletedAt;

    @Builder
    public TargetPerson(User user, String name, Relationship relation, Integer age, String phoneNumber,  LocalDate birthday, String job, String interests,
                        String events, String memo) {
        this.user = user;
        this.name = name;
        this.relation = relation;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.job = job;
        this.interests = interests;
        this.events = events;
        this.memo = memo;
    }

    public void update(String name, Relationship relation, Integer age, String phoneNumber,
                       LocalDate birthday, String job, String interests, String events, String memo) {
        this.name = name;
        this.relation = relation;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.job = job;
        this.interests = interests;
        this.events = events;
        this.memo = memo;
    }
}