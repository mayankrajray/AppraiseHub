package com.appraisehub.exception;


//Thrown when someone tries to move appraisal to wrong status.
//Returns 400 Bad Request status code.
public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}