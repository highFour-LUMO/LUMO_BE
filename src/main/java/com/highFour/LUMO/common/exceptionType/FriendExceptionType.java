package com.highFour.LUMO.common.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum FriendExceptionType implements ExceptionType {

    FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND, "등록된 친구가 없습니다."),
    ALREADY_REQUESTED(HttpStatus.BAD_REQUEST, "이미 상대에게 친구 요청을 보냈습니다."),
    ALREADY_FRIENDS(HttpStatus.BAD_REQUEST, "이미 상대와 친구 상태입니다.");



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
