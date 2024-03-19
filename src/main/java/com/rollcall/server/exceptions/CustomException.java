package com.rollcall.server.exceptions;

public class CustomException extends RuntimeException {
    int code;
    public CustomException(String message, int code) {
        super(message);
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}
