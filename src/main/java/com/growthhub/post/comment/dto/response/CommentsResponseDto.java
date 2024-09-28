package com.growthhub.post.comment.dto.response;

import com.growthhub.global.dto.response.UserResponse;
import com.growthhub.post.comment.domain.Comment;
import lombok.Builder;

import java.util.List;

@Builder
public record CommentsResponseDto(
        List<CommentWithUser> comments,
        boolean hasNext
) {

    public static CommentsResponseDto from(List<CommentWithUser> comments, boolean hasNext) {
        return CommentsResponseDto.builder()
                .comments(comments)
                .hasNext(hasNext)
                .build();
    }

    @Builder
    public record CommentWithUser(
            Long userId,
            String name,
            String nickname,
            String profileImageUrl,

            Long commentId,
            String content
    ) {

        public static CommentWithUser from(UserResponse userResponse, Comment comment) {
            return CommentWithUser.builder()
                    .userId(userResponse.userId())
                    .name(userResponse.name())
                    .nickname(userResponse.nickname())
                    .profileImageUrl(userResponse.profileImageUrl())
                    .commentId(comment.getId())
                    .content(comment.getContent())
                    .build();
        }
    }
}
