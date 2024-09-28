package com.growthhub.post.service;

import com.growthhub.global.client.UserClient;
import com.growthhub.global.dto.response.UserResponse;
import com.growthhub.global.exception.PostException;
import com.growthhub.post.domain.Post;
import com.growthhub.post.dto.request.PostRequestDto;
import com.growthhub.post.dto.response.PostResponseDto;
import com.growthhub.post.dto.response.PostsResponseDto;
import com.growthhub.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.growthhub.global.exception.errorcode.PostErrorCode.*;
import static java.util.stream.Collectors.toMap;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    private final UserClient userClient;

    @Transactional
    public PostResponseDto save(Long userId, PostRequestDto postRequestDto) {
        Post post = postRequestDto.toPost(userId);
        postRepository.save(post);
        return PostResponseDto.from(post);
    }

    public PostsResponseDto getPosts(Pageable pageable) {
        Slice<Post> postsSlice = postRepository.findAllSlice(pageable);
        List<Long> userIds = postsSlice.stream()
                .map(Post::getUserId)
                .distinct()
                .toList();
        List<UserResponse> users = userClient.getUser(userIds);
        Map<Long, UserResponse> userMap = users.stream()
                .collect(toMap(UserResponse::userId, userResponse -> userResponse));
        List<PostsResponseDto.PostWithUser> posts = postsSlice.stream().map(post -> {
            UserResponse userResponse = userMap.get(post.getUserId());
            return PostsResponseDto.PostWithUser.from(userResponse, post);
        }).toList();
        return PostsResponseDto.from(posts, postsSlice.hasNext());
    }

    public PostResponseDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
        return PostResponseDto.from(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long userId, Long postId, PostRequestDto postRequestDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
        if (!Objects.equals(post.getUserId(), userId)) {
            throw new PostException(POST_USER_MISMATCH);
        }
        if (postRequestDto.title() != null) {
            post.updateTitle(postRequestDto.title());
        }
        if (postRequestDto.content() != null) {
            post.updateContent(postRequestDto.content());
        }
        return PostResponseDto.from(post);
    }

    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(POST_NOT_FOUND));
        if (!Objects.equals(post.getUserId(), userId)) {
            throw new PostException(POST_USER_MISMATCH);
        }
        postRepository.delete(post);
    }
}
