package com.growthhub.file.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "file")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "path", length = 50, nullable = false)
    private String path;

    @Column(name = "url", nullable = false)
    private String url;

    @Builder
    public File(String name, String originalName, String path, String url) {
        this.name = name;
        this.originalName = originalName;
        this.path = path;
        this.url = url;
    }
}
