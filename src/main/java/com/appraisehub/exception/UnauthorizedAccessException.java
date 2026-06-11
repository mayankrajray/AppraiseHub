package com.appraisehub.exception;

//Thrown when someone tries to access something they should not.
//Returns 403 Forbidden status code.
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}