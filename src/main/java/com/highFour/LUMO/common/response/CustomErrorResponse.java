package com.highFour.LUMO.common.response;

import com.highFour.LUMO.common.exceptionType.ExceptionType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomErrorResponse {
	String name; // exception의 이름
	int httpStatusCode; // http 상태코드
	String message; // 에러 메시지

	public static CustomErrorResponse of(ExceptionType e) {
		return CustomErrorResponse.builder()
			.name(e.name())
			.httpStatusCode(e.httpStatus().value())
			.message(e.message())
			.build();
	}
}
