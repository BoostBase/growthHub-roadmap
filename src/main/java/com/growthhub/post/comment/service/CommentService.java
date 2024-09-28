package com.growthhub.post.comment.service;

import com.growthhub.global.client.UserClient;
import com.growthhub.global.dto.response.UserResponse;
import com.growthhub.global.exception.CommentException;
import com.growthhub.global.exception.PostException;
import com.growthhub.post.comment.domain.Comment;
import com.growthhub.post.comment.dto.request.CommentRequestDto;
import com.growthhub.post.comment.dto.response.CommentsResponseDto;
import com.growthhub.post.comment.repository.CommentRepository;
import com.growthhub.post.domain.Post;
import com.growthhub.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.growthhub.global.exception.errorcode.CommentErrorCode.*;
import static com.growthhub.global.exception.errorcode.PostErrorCode.POST_NOT_FOUND;
import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final UserClient userClient;

    private static final String DELETE_MESSAGE = "삭제된 댓글입니다.";

    @Transactional
    public void save(Long userId, CommentRequestDto commentRequestDto) {
        Comment parent = findParentCommentIfExists(commentRequestDto.parentId());
        Post post = postRepository.findById(commentRequestDto.postId())
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
        Comment comment = commentRepository.save(commentRequestDto.toComment(post, parent, userId));
        comment.updateGroupId(commentRequestDto.parentId() == null ? comment.getId() : parent.getId());
    }

    private Comment findParentCommentIfExists(Long parentId) {
        if (parentId == null) {
            return null;
        }
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));
        if (parent.getParent() != null) {
            throw new CommentException(COMMENT_DEPTH_EXCEEDED);
        }
        return parent;
    }

    public CommentsResponseDto getComments(Long postId, Pageable pageable) {
        Slice<Comment> commentsSlice = commentRepository.findAllSlice(postId, pageable);
        List<Long> userIds = commentsSlice.stream()
                .map(Comment::getUserId)
                .distinct()
                .toList();
        List<UserResponse> users = userClient.getUser(userIds);
        Map<Long, UserResponse> userMap = users.stream()
                .collect(toMap(UserResponse::userId, userResponse -> userResponse));
        List<CommentsResponseDto.CommentWithUser> comments = commentsSlice.stream().map(comment -> {
            UserResponse userResponse = userMap.get(comment.getUserId());
            return CommentsResponseDto.CommentWithUser.from(userResponse, comment);
        }).toList();
        return CommentsResponseDto.from(comments, commentsSlice.hasNext());
    }

    @Transactional
    public void updateComment(Long userId, Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));
        if (!Objects.equals(comment.getUserId(), userId)) {
            throw new CommentException(COMMENT_USER_MISMATCH);
        }
        if (commentRequestDto.content() != null) {
            comment.updateContent(commentRequestDto.content());
        }
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));
        if (!Objects.equals(comment.getUserId(), userId)) {
            throw new CommentException(COMMENT_USER_MISMATCH);
        }
        comment.updateContent(DELETE_MESSAGE);
    }
}
