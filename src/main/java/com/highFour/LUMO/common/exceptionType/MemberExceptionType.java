package com.highFour.LUMO.common.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberExceptionType implements ExceptionType {
    MEMBER_ALREADY_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "이미 존재하는 회원입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String message() {
        return message;
    }
}
