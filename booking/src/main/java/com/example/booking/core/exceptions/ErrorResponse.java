package com.example.booking.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
//@AllArgsConstructor
@NoArgsConstructor
//@RequiredArgsConstructor
public class ErrorResponse {

    private int statusCode;
    private String message;
    private List<String> errors;


    public ErrorResponse(String message)
    {
        super();
        this.message = message;
    }

    public ErrorResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public ErrorResponse(int statusCode, String message, List<String> errors) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
    }
}

