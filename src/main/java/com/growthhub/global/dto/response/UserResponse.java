package com.growthhub.global.dto.response;

import lombok.Builder;

@Builder
public record UserResponse(
        Long userId,
        String name,
        String nickname,
        String profileImageUrl,
        Long careerYear,
        String association,
        String part
) {

}
