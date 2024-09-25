package com.growthhub.post.dto.response;

import com.growthhub.post.domain.Post;
import lombok.Builder;

@Builder
public record PostResponseDto(
        Long postId,
        String title,
        String content,
        Long userId
) {

    public static PostResponseDto from(Post post) {
        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUserId())
                .build();
    }
}
