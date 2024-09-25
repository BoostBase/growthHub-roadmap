package com.growthhub.roadmap.service;

import com.growthhub.global.client.RatingClient;
import com.growthhub.global.client.UserClient;
import com.growthhub.global.dto.response.RatingResponse;
import com.growthhub.global.dto.response.UserResponse;
import com.growthhub.global.exception.DuplicationRoadmapException;
import com.growthhub.global.exception.RoadmapNotFoundException;
import com.growthhub.roadmap.domain.Roadmap;
import com.growthhub.roadmap.dto.request.RoadmapRequestDto;
import com.growthhub.roadmap.dto.response.RoadmapResponseDto;
import com.growthhub.roadmap.dto.response.RoadmapsResponseDto;
import com.growthhub.roadmap.repository.RoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.growthhub.global.exception.errorcode.RoadmapErrorCode.*;
import static java.util.stream.Collectors.toMap;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;

    private final UserClient userClient;
    private final RatingClient ratingClient;

    @Transactional
    public RoadmapResponseDto save(Long userId, RoadmapRequestDto roadmapRequestDto) {
        roadmapRepository.findByUserId(userId).ifPresent(roadmap -> {
            throw new DuplicationRoadmapException(DUPLICATION_ROADMAP);
        });
        Roadmap roadmap = roadmapRequestDto.toRoadmap(userId);
        roadmapRepository.save(roadmap);
        return RoadmapResponseDto.from(roadmap);
    }

    public RoadmapsResponseDto getRoadmaps(Pageable pageable) {
        Slice<Roadmap> roadmapsSlice = roadmapRepository.findAllSlice(pageable);
        List<Long> mentorIds = roadmapsSlice.stream()
                .map(Roadmap::getUserId)
                .distinct()
                .toList();
        List<UserResponse> mentors = userClient.getUser(mentorIds);
        Map<Long, UserResponse> mentorMap = mentors.stream()
                .collect(toMap(UserResponse::userId, userResponse -> userResponse));
        Map<Long, Double> userRatingMap = getAverageRatingMap(mentorIds);
        List<RoadmapsResponseDto.RoadmapWithUser> roadmaps = roadmapsSlice.stream().map(roadmap -> {
            UserResponse userResponse = mentorMap.get(roadmap.getUserId());
            Double rating = userRatingMap.getOrDefault(roadmap.getUserId(), 0.0);
            return RoadmapsResponseDto.RoadmapWithUser.from(userResponse, rating, roadmap);
        }).toList();
        return RoadmapsResponseDto.from(roadmaps, roadmapsSlice.hasNext());
    }

    private Map<Long, Double> getAverageRatingMap(List<Long> mentorIds) {
        List<RatingResponse> ratings = ratingClient.getRatingAverage(mentorIds);
        return ratings.stream()
                .collect(toMap(RatingResponse::mentorId, RatingResponse::rating));
    }

    public RoadmapResponseDto getRoadmap(Long roadmapId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new RoadmapNotFoundException(ROADMAP_NOT_FOUND));
        return RoadmapResponseDto.from(roadmap);
    }

    @Transactional
    public RoadmapResponseDto updateRoadmap(Long userId, RoadmapRequestDto roadMapRequestDto) {
        Roadmap roadmap = roadmapRepository.findByUserId(userId)
                .orElseThrow(() -> new RoadmapNotFoundException(ROADMAP_NOT_FOUND));
        if (roadMapRequestDto.title() != null) {
            roadmap.updateTitle(roadMapRequestDto.title());
        }
        if (roadMapRequestDto.intro() != null) {
            roadmap.updateIntro(roadMapRequestDto.intro());
        }
        if (roadMapRequestDto.content() != null) {
            roadmap.updateContent(roadMapRequestDto.content());
        }
        return RoadmapResponseDto.from(roadmap);
    }

    @Transactional
    public void deleteRoadmap(Long userId) {
        Roadmap roadmap = roadmapRepository.findByUserId(userId)
                .orElseThrow(() -> new RoadmapNotFoundException(ROADMAP_NOT_FOUND));
        roadmapRepository.delete(roadmap);
    }

    public Long getRoadmapIdByUser(Long userId) {
        Optional<Roadmap> roadmap = roadmapRepository.findByUserId(userId);
        return roadmap.map(Roadmap::getId).orElse(null);
    }
}
