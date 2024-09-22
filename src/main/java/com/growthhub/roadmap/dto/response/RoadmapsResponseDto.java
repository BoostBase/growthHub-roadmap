package com.growthhub.roadmap.dto.response;

import com.growthhub.global.dto.response.UserResponse;
import com.growthhub.roadmap.domain.Roadmap;
import lombok.Builder;

import java.util.List;

@Builder
public record RoadmapsResponseDto(
        List<RoadmapWithUser> roadmaps,
        boolean hasNext
) {

    public static RoadmapsResponseDto from(List<RoadmapWithUser> roadmaps, boolean hasNext) {
        return RoadmapsResponseDto.builder()
                .roadmaps(roadmaps)
                .hasNext(hasNext)
                .build();
    }

    @Builder
    public record RoadmapWithUser(
            Long mentorId,
            String name,
            String nickname,
            String profileImageUrl,
            Long careerYear,
            String association,
            String part,
            Double rating,
            Long roadMapId,
            String title
    ) {
        public static RoadmapWithUser from(UserResponse userResponse, Double rating, Roadmap roadMap) {
            return RoadmapWithUser.builder()
                    .mentorId(userResponse.mentorId())
                    .name(userResponse.name())
                    .nickname(userResponse.nickname())
                    .profileImageUrl(userResponse.profileImageUrl())
                    .careerYear(userResponse.careerYear())
                    .association(userResponse.association())
                    .part(userResponse.part())
                    .rating(rating)
                    .roadMapId(roadMap.getId())
                    .title(roadMap.getTitle())
                    .build();
        }
    }
}
