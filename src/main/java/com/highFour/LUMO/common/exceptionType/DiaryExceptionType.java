package com.highFour.LUMO.common.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DiaryExceptionType implements ExceptionType {
	DIARY_ALREADY_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "이미 오늘의 일기를 작성했습니다."),
	EMOTION_NOT_FOUND(HttpStatus.NOT_FOUND, "감정 정보가 존재하지 않습니다."),
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리 정보가 존재하지 않습니다."),
	DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "일기 정보가 존재하지 않습니다.");


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
