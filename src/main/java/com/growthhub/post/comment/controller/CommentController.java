package com.growthhub.post.comment.controller;

import com.growthhub.global.dto.ResponseTemplate;
import com.growthhub.post.comment.dto.request.CommentRequestDto;
import com.growthhub.post.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 저장", description = "Authorization Header: JWT Token")
    @PostMapping
    public ResponseEntity<ResponseTemplate<?>> save(
            HttpServletRequest request,
            @RequestBody CommentRequestDto commentRequestDto
    ) {
        Long userId = Long.parseLong(request.getHeader("User-Id"));
        commentService.save(userId, commentRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.EMPTY_RESPONSE);
    }

    @Operation(summary = "댓글 리스트 조회")
    @GetMapping
    public ResponseEntity<ResponseTemplate<Object>> getComments(
            @RequestParam("postId") Long postId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.from(commentService.getComments(postId, pageable)));
    }

    @Operation(summary = "댓글 수정", description = "Authorization Header: JWT Token")
    @PutMapping("/{commentId}")
    public ResponseEntity<ResponseTemplate<?>> updateComment(
            HttpServletRequest request,
            @PathVariable("commentId") Long commentId,
            @RequestBody CommentRequestDto commentRequestDto
    ) {
        Long userId = Long.parseLong(request.getHeader("User-Id"));
        commentService.updateComment(userId, commentId, commentRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.EMPTY_RESPONSE);
    }

    @Operation(summary = "댓글 삭제", description = "Authorization Header: JWT Token")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseTemplate<?>> deleteComment(
            HttpServletRequest request,
            @PathVariable("commentId") Long commentId
    ) {
        Long userId = Long.parseLong(request.getHeader("User-Id"));
        commentService.deleteComment(userId, commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.EMPTY_RESPONSE);
    }
}
