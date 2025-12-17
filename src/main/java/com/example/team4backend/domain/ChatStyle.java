package com.example.team4backend.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_styles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String styleName;

    @Column(columnDefinition = "TEXT")
    private String description;

    public ChatStyle(String styleName, String description) {
        this.styleName = styleName;
        this.description = description;
    }
}
