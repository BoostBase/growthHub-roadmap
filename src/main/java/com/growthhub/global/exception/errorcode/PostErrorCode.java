package com.growthhub.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post not found."),
    POST_USER_MISMATCH(HttpStatus.BAD_REQUEST, "Post User mismatch.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
