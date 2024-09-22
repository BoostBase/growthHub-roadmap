package com.growthhub.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoadmapErrorCode implements ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found."),
    ROADMAP_NOT_FOUND(HttpStatus.NOT_FOUND, "Roadmap not found."),
    DUPLICATION_ROADMAP(HttpStatus.BAD_REQUEST, "Roadmap already exists.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
