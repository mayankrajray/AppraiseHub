package com.appraisehub.exception;

//Thrown when someone tries to create something that already exists.
//Returns 409 Conflict status code.
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}