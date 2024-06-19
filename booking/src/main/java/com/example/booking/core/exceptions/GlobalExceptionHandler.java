package com.example.booking.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value
            = TokenInvalidException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody ErrorResponse
    handleTokenExpiredException(
            TokenInvalidException ex) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage());
    }

    @ExceptionHandler(value
            = AuthenticationFailedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody ErrorResponse
    handleAuthenticationFailedException(
            AuthenticationFailedException ex) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage());
    }

    @ExceptionHandler(value
            = RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse
    handleRecordNotFoundException(
            RecordNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                ex.getMessage());
    }

    @ExceptionHandler(value
            = RecordAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody ErrorResponse
    handleRecordAlreadyExistsException(
            RecordAlreadyExistsException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(),
                ex.getMessage());
    }

    @ExceptionHandler(value
            = FileCouldNotBeSavedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse
    handleFileCouldNotBeSavedException(
            FileCouldNotBeSavedException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                ex.getMessage());
    }


}
