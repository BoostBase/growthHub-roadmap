package com.growthhub.post.comment.dto.request;

import com.growthhub.post.comment.domain.Comment;
import com.growthhub.post.domain.Post;

public record CommentRequestDto(
        String content,
        Long postId,
        Long parentId
) {

    public Comment toComment(Post post, Comment parent, Long userId) {
        return Comment.builder()
                .content(content)
                .userId(userId)
                .post(post)
                .parent(parent)
                .build();
    }
}
