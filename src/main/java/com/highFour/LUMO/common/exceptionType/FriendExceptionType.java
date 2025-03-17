package com.highFour.LUMO.common.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum FriendExceptionType implements ExceptionType {

    FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 친구가 없습니다."),
    ALREADY_REQUESTED(HttpStatus.BAD_REQUEST, "해당 친구와의 요청이 존재합니다."),
    ALREADY_FRIENDS(HttpStatus.BAD_REQUEST, "이미 상대와 친구 상태입니다."),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 친구 요청을 찾을 수 없습니다."),
    INVALID_REQUEST_STATUS(HttpStatus.BAD_REQUEST,"유효하지 않은 친구 요청 상태입니다.");



    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return status;
    }

    @Override
    public String message() {
        return message;
    }
}
