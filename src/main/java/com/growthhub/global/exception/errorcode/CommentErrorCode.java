package com.growthhub.global.exception.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment not found."),
    COMMENT_DEPTH_EXCEEDED(HttpStatus.BAD_REQUEST, "Comment depth exceeds."),
    COMMENT_USER_MISMATCH(HttpStatus.BAD_REQUEST, "Comment User mismatch.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
