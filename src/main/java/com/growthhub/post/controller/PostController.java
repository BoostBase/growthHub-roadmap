package com.growthhub.post.controller;

import com.growthhub.global.dto.ResponseTemplate;
import com.growthhub.post.dto.request.PostRequestDto;
import com.growthhub.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<ResponseTemplate<Object>> save(
            HttpServletRequest request,
            @RequestBody PostRequestDto postRequestDto
    ) {
        Long userId = Long.parseLong(request.getHeader("User-Id"));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.from(postService.save(userId, postRequestDto)));
    }

    @GetMapping
    public ResponseEntity<ResponseTemplate<Object>> getPosts(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.from(postService.getPosts(pageable)));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ResponseTemplate<Object>> getPost(
            @PathVariable("postId") Long postId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.from(postService.getPost(postId)));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<ResponseTemplate<Object>> updatePost(
            HttpServletRequest request,
            @PathVariable("postId") Long postId,
            @RequestBody PostRequestDto postRequestDto
    ) {
        Long userId = Long.parseLong(request.getHeader("User-Id"));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.from(postService.updatePost(userId, postId, postRequestDto)));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ResponseTemplate<?>> deletePost(
            HttpServletRequest request,
            @PathVariable("postId") Long postId
    ) {
        Long userId = Long.parseLong(request.getHeader("User-Id"));
        postService.deletePost(userId, postId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.EMPTY_RESPONSE);
    }
}
