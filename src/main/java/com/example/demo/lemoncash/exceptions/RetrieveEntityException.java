package com.example.demo.lemoncash.exceptions;

public class RetrieveEntityException extends RuntimeException {

    public RetrieveEntityException(String message) {
        super(message);
    }

    public RetrieveEntityException(String message, Throwable e) {
        super(message, e);
    }

}
