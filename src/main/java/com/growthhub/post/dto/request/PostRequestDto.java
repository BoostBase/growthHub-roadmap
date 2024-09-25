package com.growthhub.post.dto.request;

import com.growthhub.post.domain.Post;

public record PostRequestDto(
        String title,
        String content
) {

    public Post toPost(Long userId) {
        return Post.builder()
                .title(title)
                .content(content)
                .userId(userId)
                .build();
    }
}
