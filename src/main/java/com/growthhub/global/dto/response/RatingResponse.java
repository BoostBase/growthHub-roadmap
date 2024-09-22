package com.growthhub.global.dto.response;

import lombok.Builder;

@Builder
public record RatingResponse(
        Long mentorId,
        Double rating
) {

}
