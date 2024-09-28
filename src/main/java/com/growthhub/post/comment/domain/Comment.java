package com.growthhub.post.comment.domain;

import com.growthhub.post.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // 댓글의 부모 (대댓글일 경우 부모 댓글)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    // 부모 댓글에 달린 대댓글들
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Builder
    public Comment(String content, Long userId, Post post, Comment parent) {
        this.content = content;
        this.userId = userId;
        this.post = post;
        this.parent = parent;
        this.groupId = 0L;
        this.children = new ArrayList<>();
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
