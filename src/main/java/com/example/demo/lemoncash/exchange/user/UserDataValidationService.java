package com.example.demo.lemoncash.exchange.user;

import com.example.demo.lemoncash.exceptions.CreateEntityException;

import static com.example.demo.lemoncash.utils.CreationValidationService.createCheckIsNotNullRequiredField;

public abstract class UserDataValidationService {

    private static String baseMessage = "Error while validating entity User for insert, attribute '%s' must not be empty";

    public static void checkAttributesForInsert(User user) throws CreateEntityException {
        createCheckIsNotNullRequiredField(user.getName(), baseMessage, "name");
        createCheckIsNotNullRequiredField(user.getSurname(), baseMessage, "surname");
        createCheckIsNotNullRequiredField(user.getEmail(), baseMessage, "email");
        createCheckIsNotNullRequiredField(user.getAlias(), baseMessage, "alias");
    }

}
