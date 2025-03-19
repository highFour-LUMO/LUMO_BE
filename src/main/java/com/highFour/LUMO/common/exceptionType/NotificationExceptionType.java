package com.highFour.LUMO.common.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotificationExceptionType implements ExceptionType{
	SECRET_FILE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "FCM 시크릿 파일이 없습니다."),
	INVALID_SECRET_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 형식의 FCM 시크릿 파일입니다."),
	FCM_TOKEN_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "유저의 FCM 토큰이 존재하지 않습니다."),
	FCM_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "메시지 전송에 실패했습니다.");

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
