package com.growthhub.global.exception;

import com.growthhub.global.exception.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostUserMismatchException extends RuntimeException {

    private final ErrorCode errorCode;
}
