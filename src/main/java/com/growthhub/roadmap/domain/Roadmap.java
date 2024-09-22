package com.growthhub.roadmap.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "roadmap")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "intro")
    private String intro;

    @Lob
    @Column(name = "content", columnDefinition = "LONGTEXT", nullable = false)
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder
    public Roadmap(String title, String intro, String content, Long userId) {
        this.title = title;
        this.intro = intro;
        this.content = content;
        this.userId = userId;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateIntro(String intro) {
        this.intro = intro;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
