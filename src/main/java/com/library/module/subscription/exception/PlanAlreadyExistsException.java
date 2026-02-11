package com.library.module.subscription.exception;

public class PlanAlreadyExistsException extends RuntimeException{
    public PlanAlreadyExistsException(String message) {
        super(message);
    }
}
