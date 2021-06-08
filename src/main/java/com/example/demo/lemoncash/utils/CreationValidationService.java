package com.example.demo.lemoncash.utils;

import com.example.demo.lemoncash.exceptions.CreateEntityException;

import static java.util.Objects.isNull;

public class CreationValidationService {

    public static void createCheckIsNotNullRequiredField(Object value, String message, String attribute) throws CreateEntityException {
        if (isNull(value)) {
            throw new CreateEntityException(String.format(message, attribute));
        }
    }
}