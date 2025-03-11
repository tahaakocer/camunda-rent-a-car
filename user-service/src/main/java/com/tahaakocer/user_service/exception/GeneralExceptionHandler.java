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
    public ResponseEntity<GeneralResponse> handleGeneralException(GeneralException ex) {
        HttpStatus status = ex.getHttpStatus() != null ? ex.getHttpStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
        GeneralResponse errorResponse = GeneralResponse.builder()
                .code(status.value())
                .message(ex.getMessage())
                .build();
        log.error("Hata: {} - HTTP Kodu: {}", ex.getMessage(), status.value(), ex);
        return new ResponseEntity<>(errorResponse, status);
    }

    // Diğer exception türleri için generic bir handler (isteğe bağlı)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResponse> handleAllExceptions(Exception ex) {
        GeneralResponse errorResponse = GeneralResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Beklenmeyen bir hata oluştu: " + ex.getMessage())
                .build();
        log.error("Beklenmeyen hata: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}