package com.growthhub.post.comment.repository;

import com.growthhub.post.comment.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.groupId ASC, c.id ASC")
    Slice<Comment> findAllSlice(@Param("postId") Long postId, Pageable pageable);
}
