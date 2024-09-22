package com.growthhub.roadmap.dto.response;

import com.growthhub.roadmap.domain.Roadmap;
import lombok.Builder;

@Builder
public record RoadmapResponseDto(
        Long roadMapId,
        String title,
        String intro,
        String content,
        Long mentorId
) {

    public static RoadmapResponseDto from(Roadmap roadMap) {
        return RoadmapResponseDto.builder()
                .roadMapId(roadMap.getId())
                .title(roadMap.getTitle())
                .intro(roadMap.getIntro())
                .content(roadMap.getContent())
                .mentorId(roadMap.getUserId())
                .build();
    }
}
