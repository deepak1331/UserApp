package com.learn.UserApp.exception;

import com.learn.UserApp.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException exception) {
        ApiResponse response = ApiResponse.builder().message(exception.getMessage())
                .success(Boolean.TRUE).status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({RuntimeException.class, Exception.class})
    public ResponseEntity<ApiResponse> handleRuntimeException(Exception exception) {
        ApiResponse response = ApiResponse.builder().message(exception.getMessage())
                .success(Boolean.FALSE).status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        ApiResponse response = ApiResponse.builder().message(exception.getMessage())
                .success(Boolean.FALSE).status(HttpStatus.BAD_GATEWAY).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }
}
