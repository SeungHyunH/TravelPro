package com.sirokuma.travelpro.exception;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(int code, String message) {
        super(code, message);
    }
}
