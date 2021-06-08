package com.example.demo.lemoncash.exchange.movement;

import com.example.demo.lemoncash.exceptions.CreateEntityException;

import java.security.InvalidParameterException;

import static com.example.demo.lemoncash.utils.CreationValidationService.createCheckIsNotNullRequiredField;

public abstract class MovementDataValidationService {

    private static String baseMessage = "Error while validating entity Movement for insert, attribute '%s' must not be empty";

    public static void checkAttributesForInsert(Movement movement) throws CreateEntityException {

        createCheckIsNotNullRequiredField(movement.getMovementType(), baseMessage, "movementType");
        createCheckIsNotNullRequiredField(movement.getAmount(), baseMessage, "amount");
        createCheckIsNotNullRequiredField(movement.getCoinTypeId(), baseMessage, "coinTypeId");

        switch (movement.getMovementType()) {
            case DEPOSIT:
                createCheckIsNotNullRequiredField(movement.getDestinationWalletId(), baseMessage, "destinationWalletId");
                break;
            case EXTRACT:
                createCheckIsNotNullRequiredField(movement.getOriginWalletId(), baseMessage, "originWalletId");
                break;
            case TRANSFER:
                createCheckIsNotNullRequiredField(movement.getDestinationWalletId(), baseMessage, "destinationWalletId");
                createCheckIsNotNullRequiredField(movement.getOriginWalletId(), baseMessage, "originWalletId");
                break;
            default:
                throw new InvalidParameterException();
        }
    }
}
