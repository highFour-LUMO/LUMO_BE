package com.highFour.LUMO.common.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommentExceptionType implements ExceptionType {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
    UNAUTHORIZED_COMMENT_EDIT(HttpStatus.UNAUTHORIZED, "댓글 작성자만 수정할 수 있습니다.");



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
