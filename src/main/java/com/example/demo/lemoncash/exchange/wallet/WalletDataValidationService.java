package com.example.demo.lemoncash.exchange.wallet;

import com.example.demo.lemoncash.exceptions.CreateEntityException;

import static com.example.demo.lemoncash.utils.CreationValidationService.createCheckIsNotNullRequiredField;

public abstract class WalletDataValidationService {

    private static String baseMessage = "Error while validating entity Wallet for insert, attribute '%s' must not be empty";

    public static void checkAttributesForInsert(Wallet wallet) throws CreateEntityException {
        createCheckIsNotNullRequiredField(wallet.getBalance(), baseMessage, "balance");
        createCheckIsNotNullRequiredField(wallet.getCoinTypeId(), baseMessage, "coinTypeId");
    }

}
