package com.tahaakocer.user_service.exception;

import com.tahaakocer.user_service.dto.GeneralResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<GeneralResponse> handleStartProcessException(GeneralException ex) {
        GeneralResponse errorResponse = GeneralResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .data(null)
                .build();
        log.error("{}:{}", errorResponse.getMessage(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
