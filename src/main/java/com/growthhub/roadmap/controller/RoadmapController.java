package com.growthhub.roadmap.controller;

import com.growthhub.global.dto.ResponseTemplate;
import com.growthhub.roadmap.dto.request.RoadmapRequestDto;
import com.growthhub.roadmap.service.RoadmapService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapService roadmapService;

    @Operation(summary = "로드맵 저장", description = "Authorization Header: JWT Token")
    @PostMapping
    public ResponseEntity<ResponseTemplate<Object>> save(
            HttpServletRequest request,
            @Valid @RequestBody RoadmapRequestDto roadmapRequestDto
    ) {
        Long userId = Long.parseLong(request.getHeader("User-Id"));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.from(roadmapService.save(userId, roadmapRequestDto)));
    }

    @Operation(summary = "로드맵 리스트 조회", description = "default page: 10")
    @GetMapping
    public ResponseEntity<ResponseTemplate<Object>> getRoadmaps(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.from(roadmapService.getRoadmaps(pageable)));
    }

    @Operation(summary = "로드맵 조회", description = "Path Parameter: roadMapId")
    @GetMapping("/{roadmapId}")
    public ResponseEntity<ResponseTemplate<Object>> getRoadmap(
            @PathVariable(value = "roadmapId") Long roadmapId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.from(roadmapService.getRoadmap(roadmapId)));
    }

    @Operation(summary = "로드맵 수정", description = "Authorization Header: JWT Token")
    @PutMapping
    public ResponseEntity<ResponseTemplate<Object>> updateRoadmap(
            HttpServletRequest request,
            @RequestBody RoadmapRequestDto roadmapRequestDto
    ) {
        Long userId = Long.parseLong(request.getHeader("User-Id"));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.from(roadmapService.updateRoadmap(userId, roadmapRequestDto)));
    }

    @Operation(summary = "로드맵 삭제", description = "Authorization Header: JWT Token")
    @DeleteMapping
    public ResponseEntity<ResponseTemplate<?>> deleteRoadmap(
            HttpServletRequest request
    ) {
        Long userId = Long.parseLong(request.getHeader("User-Id"));
        roadmapService.deleteRoadmap(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.EMPTY_RESPONSE);
    }

    @Operation(summary = "멘토 로드맵 아이디 조회", description = "Authorization Header: JWT Token")
    @GetMapping("/mentor")
    public ResponseEntity<ResponseTemplate<Object>> getRoadmapByUser(
            HttpServletRequest request
    ) {
        Long userId = Long.parseLong(request.getHeader("User-Id"));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.from(roadmapService.getRoadmapIdByUser(userId)));
    }

    @GetMapping("/mentors")
    public ResponseEntity<ResponseTemplate<Object>> getRoadmapByUsers(
            @RequestParam List<Long> userIds
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseTemplate.from(roadmapService.getRoadmapIdByUserList(userIds)));
    }
}
