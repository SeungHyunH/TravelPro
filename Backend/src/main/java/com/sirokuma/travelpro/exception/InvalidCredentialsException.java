package com.sirokuma.travelpro.exception;

public class InvalidCredentialsException extends CustomException {
    public InvalidCredentialsException(int code, String message) {
        super(code, message);
    }
}
