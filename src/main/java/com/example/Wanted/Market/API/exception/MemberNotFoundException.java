package com.example.Wanted.Market.API.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long memberId) {
        super("Could not find member with ID: " + memberId);
    }
}
