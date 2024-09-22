package com.growthhub.roadmap.repository;

import com.growthhub.roadmap.domain.Roadmap;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {

//    @Query("SELECT r FROM Roadmap r JOIN FETCH r.user WHERE r.id = :id")
//    Optional<Roadmap> findByIdWithFetch(@Param("id") Long id);
//
//    @Query("SELECT r FROM Roadmap r JOIN FETCH r.user WHERE r.user.id = :userId")
//    Optional<Roadmap> findByUserIdWithFetch(@Param("userId") Long userId);
//
//    @EntityGraph(attributePaths = {"user"})
//    @Query("SELECT r FROM Roadmap r JOIN FETCH r.user u ORDER BY r.id DESC")
//    Slice<Roadmap> findAllSlice(Pageable pageable);

    @Query("SELECT r FROM Roadmap r WHERE r.userId = :userId")
    Optional<Roadmap> findByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Roadmap r ORDER BY r.id DESC")
    Slice<Roadmap> findAllSlice(Pageable pageable);
}
