package com.example.team4backend.domain;

import com.example.team4backend.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relationship_id", nullable = false)
    private Relationship relationship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_style_id")
    private ChatStyle chatStyle;

    private Instant deletedAt;

    @Builder
    public TargetPerson(User user, String name, Relationship relationship, ChatStyle chatStyle, Integer age, String phoneNumber,  LocalDate birthday, String job, String interests,
                        String events, String memo) {
        this.user = user;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.job = job;
        this.interests = interests;
        this.events = events;
        this.memo = memo;
        this.relationship = relationship;
        this.chatStyle = chatStyle;
        this.deletedAt = null;
    }

    public void update(
            String name,
            Relationship relationship,
            ChatStyle chatStyle,
            Integer age,
            String phoneNumber,
            LocalDate birthday,
            String job,
            String interests,
            String events,
            String memo
    ) {
        this.name = name;
        this.relationship = relationship;
        this.chatStyle = chatStyle;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.job = job;
        this.interests = interests;
        this.events = events;
        this.memo = memo;
    }
}