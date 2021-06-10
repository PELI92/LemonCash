package com.example.demo.lemoncash.exceptions;

import java.sql.SQLException;

public class CreateEntityException extends SQLException {

    public CreateEntityException(String message) {
        super(message);
    }

    public CreateEntityException(String message, Throwable e) {
        super(message, e);
    }

}
