package com.tahaakocer.camunda.exception;

public class StartProcessException extends RuntimeException {
    public StartProcessException(String s, Exception e) {
        super(s, e);
    }
}
