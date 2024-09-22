package com.growthhub.roadmap.dto.request;

import com.growthhub.roadmap.domain.Roadmap;

public record RoadmapRequestDto(
        String title,
        String intro,
        String content
) {

    public Roadmap toRoadmap(Long userId) {
        return Roadmap.builder()
                .title(title)
                .intro(intro)
                .content(content)
                .userId(userId)
                .build();
    }
}
