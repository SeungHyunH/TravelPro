package com.sirokuma.travelpro.response;

import lombok.Getter;

@Getter
public enum CustomResponseCode {
    SUCCESS(200),
    ERROR(-500),
    SIGNUP_REQUIRED(-401),
    USER_NOT_FOUND(-402),
    INVALID_CREDENTIALS(-403),
    DUPLICATE_EMAIL(-404);

    private final int code;

    CustomResponseCode(int code) {
        this.code = code;
    }

}
