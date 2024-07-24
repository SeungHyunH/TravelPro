package com.sirokuma.travelpro.response;

import lombok.Getter;

@Getter
public class CustomResponse {
    private final int code;
    private final String message;

    public CustomResponse(CustomResponseCode code, String message) {
        this.code = code.getCode();
        this.message = message;
    }
}
