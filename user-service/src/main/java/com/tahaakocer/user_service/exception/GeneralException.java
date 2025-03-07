package com.tahaakocer.user_service.exception;

public class GeneralException extends RuntimeException {
    public GeneralException(String s) {
        super(s);
    }
    public GeneralException(String s, Exception e) {
        super(s, e);
    }
}
