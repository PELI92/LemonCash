package com.example.demo.lemoncash.exchange.userWallet;

import com.example.demo.lemoncash.exceptions.CreateEntityException;

import static com.example.demo.lemoncash.utils.CreationValidationService.createCheckIsNotNullRequiredField;

public abstract class UserWalletDataValidationService {

    private static String baseMessage = "Error while validating entity UserWallet for insert, attribute '%s' must not be empty";

    public static void checkAttributesForInsert(UserWallet userWallet) throws CreateEntityException {
        createCheckIsNotNullRequiredField(userWallet.getUserId(), baseMessage, "user_id");
        createCheckIsNotNullRequiredField(userWallet.getWalletId(), baseMessage, "wallet_id");
    }

}
