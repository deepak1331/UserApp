package com.learn.UserApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
public class ApiResponse {

    private String message;
    private boolean success;
    private HttpStatus status;
}
