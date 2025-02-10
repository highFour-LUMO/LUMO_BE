package com.highFour.LUMO.common.exception;

import com.highFour.LUMO.common.exceptionType.ExceptionType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class BaseCustomException extends RuntimeException{
	private final ExceptionType exceptionType;

	@Override
	public String getMessage() {
		return exceptionType.message();
	}
}
