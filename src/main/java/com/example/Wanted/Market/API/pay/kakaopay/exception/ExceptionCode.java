package com.example.Wanted.Market.API.pay.kakaopay.exception;
import lombok.Getter;

public enum ExceptionCode {
    PAY_FAILED(404, "결제가 실패되었습니다."),
    PAY_CANCEL(404, "결제가 취소되었습니다.");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
