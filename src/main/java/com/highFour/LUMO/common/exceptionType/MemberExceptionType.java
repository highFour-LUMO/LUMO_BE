package com.highFour.LUMO.common.exceptionType;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MemberExceptionType implements ExceptionType {

    INVALID_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 방식의 로그인입니다."),
    NEED_TO_REGISTER(HttpStatus.BAD_REQUEST, "회원가입이 필요합니다."),
    NOT_A_NEW_MEMBER(HttpStatus.BAD_REQUEST, "이미 가입된 회원입니다."),
    FAIL_TO_AUTH(HttpStatus.BAD_REQUEST, "인증에 실패했습니다."),
    NOT_A_NEW_NICKNAME(HttpStatus.BAD_REQUEST,"이미 존재하는 닉네임입니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원이 존재하지 않습니다."),
    DELETED_MEMBER(HttpStatus.FORBIDDEN, "탈퇴한 회원입니다."),
    NEED_TO_EMAIL_AUTH(HttpStatus.BAD_REQUEST,"이메일 인증이 필요합니다."),
    EMAIL_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "이미 인증된 이메일입니다."),
    EMAIL_AUTH_ALREADY_SENT(HttpStatus.BAD_REQUEST, "인증번호가 이미 발송되었습니다. 10분 후에 다시 시도하세요."),
    AUTH_NUMBER_EXPIRED(HttpStatus.BAD_REQUEST, "인증번호가 만료되었습니다. 새로운 인증번호를 요청하세요."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다."),
    PASSWORD_CONFIRM_MISMATCH(HttpStatus.BAD_REQUEST, "새 비밀번호와 확인용 비밀번호가 일치하지 않습니다."),
    EMAIL_SEND_FAILED(HttpStatus.BAD_REQUEST,"이메일 전송 실패했습니다.");




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
