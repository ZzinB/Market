package com.example.Wanted.Market.API.pay.kakaopay.exception;

import lombok.Getter;

public class BusinessLogicException extends RuntimeException {
    @Getter
    private final ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
