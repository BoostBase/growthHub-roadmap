package com.growthhub.post.comment.service;

import com.growthhub.global.exception.CommentDepthExceededException;
import com.growthhub.global.exception.CommentNotFoundException;
import com.growthhub.global.exception.CommentUserMismatchException;
import com.growthhub.global.exception.PostNotFoundException;
import com.growthhub.post.comment.domain.Comment;
import com.growthhub.post.comment.dto.request.CommentRequestDto;
import com.growthhub.post.comment.repository.CommentRepository;
import com.growthhub.post.domain.Post;
import com.growthhub.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.growthhub.global.exception.errorcode.CommentErrorCode.*;
import static com.growthhub.global.exception.errorcode.PostErrorCode.POST_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private static final String DELETE_MESSAGE = "삭제된 댓글입니다.";

    @Transactional
    public void save(Long userId, CommentRequestDto commentRequestDto) {
        Comment parent = findParentCommentIfExists(commentRequestDto.parentId());
        Post post = postRepository.findById(commentRequestDto.postId())
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND));
        Comment comment = commentRequestDto.toComment(post, parent, userId);
        commentRepository.save(comment);
    }

    private Comment findParentCommentIfExists(Long parentId) {
        if (parentId == null) {
            return null;
        }
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND));
        if (parent.getParent() != null) {
            throw new CommentDepthExceededException(COMMENT_DEPTH_EXCEEDED);
        }
        return parent;
    }

    @Transactional
    public void updateComment(Long userId, Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND));
        if (!Objects.equals(comment.getUserId(), userId)) {
            throw new CommentUserMismatchException(COMMENT_USER_MISMATCH);
        }
        if (commentRequestDto.content() != null) {
            comment.updateContent(commentRequestDto.content());
        }
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND));
        if (!Objects.equals(comment.getUserId(), userId)) {
            throw new CommentUserMismatchException(COMMENT_USER_MISMATCH);
        }
        comment.updateContent(DELETE_MESSAGE);
    }
}
