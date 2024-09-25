package com.growthhub.post.dto.response;

import com.growthhub.global.dto.response.UserResponse;
import com.growthhub.post.domain.Post;
import lombok.Builder;

import java.util.List;

@Builder
public record PostsResponseDto(
        List<PostWithUser> posts,
        boolean hasNext
) {

    public static PostsResponseDto from(List<PostWithUser> posts, boolean hasNext) {
        return PostsResponseDto.builder()
                .posts(posts)
                .hasNext(hasNext)
                .build();
    }

    @Builder
    public record PostWithUser(
            Long userId,
            String name,
            String nickname,
            String profileImageUrl,

            Long postId,
            String title,
            String content
    ) {

        public static PostWithUser from(UserResponse userResponse, Post post) {
            return PostWithUser.builder()
                    .userId(userResponse.userId())
                    .name(userResponse.name())
                    .nickname(userResponse.nickname())
                    .profileImageUrl(userResponse.profileImageUrl())
                    .postId(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .build();
        }
    }
}
