package com.highFour.LUMO.common.exceptionType;

import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DiaryExceptionType implements ExceptionType {
	DIARY_ALREADY_EXIST(HttpStatus.INTERNAL_SERVER_ERROR, "이미 오늘의 일기를 작성했습니다."),
	EMOTION_NOT_FOUND(HttpStatus.NOT_FOUND, "감정 정보가 존재하지 않습니다."),
	CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리 정보가 존재하지 않습니다."),
	DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "일기 정보가 존재하지 않습니다."),
	RATING_NOT_FOUND(HttpStatus.NOT_FOUND, "오늘의 점수가 존재하지 않습니다."),
	INVALID_RATING(HttpStatus.BAD_REQUEST, "오늘의 점수는 1점 ~ 5점에서 선택해주세요."),
	TITLE_TOO_LONG(HttpStatus.BAD_REQUEST, "제목이 너무 깁니다. (최대 50자)"),
	CONTENTS_TOO_LONG(HttpStatus.BAD_REQUEST, "내용이 너무 깁니다. (최대 3000자)"),
	UNAUTHORIZED_DIARY_ACCESS(HttpStatus.FORBIDDEN, "본인이 작성한 일기만 수정이 가능합니다."),
	;

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
