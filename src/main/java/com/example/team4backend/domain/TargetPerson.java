package com.example.team4backend.domain;

import com.example.team4backend.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @Column(columnDefinition = "TEXT")
    private String interests;

    @OneToMany(mappedBy = "targetPerson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> events = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relationship_id", nullable = false)
    private Relationship relationship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_style_id")
    private ChatStyle chatStyle;

    private Instant lastMessageDate;

    private Instant deletedAt;

    @Builder
    public TargetPerson(User user, String name, Relationship relationship, ChatStyle chatStyle, Integer age, String phoneNumber, LocalDate birthday, String interests) {
        this.user = user;
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.interests = interests;
        this.relationship = relationship;
        this.chatStyle = chatStyle;
        this.deletedAt = null;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public void clearEvents() {
        this.events.clear();
    }

    public void update(
            String name,
            Relationship relationship,
            ChatStyle chatStyle,
            Integer age,
            String phoneNumber,
            LocalDate birthday,
            String interests
    ) {
        this.name = name;
        this.relationship = relationship;
        this.chatStyle = chatStyle;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.interests = interests;
    }
}