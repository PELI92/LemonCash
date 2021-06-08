package com.example.demo.lemoncash.exceptions;

public class CreateEntityException extends RuntimeException {
    
    public CreateEntityException(String message) {
        super(message);
    }

    public CreateEntityException(String message, Throwable e) {
        super(message, e);
    }

}
