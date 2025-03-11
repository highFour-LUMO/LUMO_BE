package com.highFour.LUMO.common.exception;


import com.highFour.LUMO.common.exceptionType.ExceptionType;

public class TokenException extends BaseCustomException {
    public TokenException(ExceptionType exceptionType) {
        super(exceptionType);
    }
}
