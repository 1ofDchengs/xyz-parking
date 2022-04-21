package com.xyz.parking.error;

import lombok.Data;

import java.util.List;

@Data
public class ApiErrorResponse {

    private String message;
    private String details;
    private List<String> errors;

    public ApiErrorResponse(String message, String details, List<String> errors) {
        this.message = message;
        this.details = details;
        this.errors = errors;
    }
}
