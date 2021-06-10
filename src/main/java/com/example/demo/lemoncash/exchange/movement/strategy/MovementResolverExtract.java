package com.example.demo.lemoncash.exchange.movement.strategy;

import com.example.demo.lemoncash.exceptions.CreateEntityException;
import com.example.demo.lemoncash.exceptions.EntityNotFoundException;
import com.example.demo.lemoncash.exceptions.InsufficientFundsException;
import com.example.demo.lemoncash.exchange.coin.type.CoinType;
import com.example.demo.lemoncash.exchange.movement.Movement;
import com.example.demo.lemoncash.exchange.movement.MovementRequest;
import com.example.demo.lemoncash.exchange.user.User;
import com.example.demo.lemoncash.exchange.user.UserService;
import com.example.demo.lemoncash.exchange.userWallet.UserWalletDataService;
import com.example.demo.lemoncash.exchange.wallet.Wallet;
import com.example.demo.lemoncash.exchange.wallet.WalletDataService;
import lombok.Builder;
import org.springframework.stereotype.Service;

import static com.example.demo.lemoncash.exchange.movement.MovementType.extract;
import static java.util.Objects.nonNull;

@Builder
@Service
public class MovementResolverExtract extends MovementResolver {

    private final UserService userService;
    private final UserWalletDataService userWalletDataService;
    private final WalletDataService walletDataService;

    public boolean isValidMovement(MovementRequest movementRequest) {
        boolean result = (nonNull(movementRequest.getUserIdOrigin())
                || nonNull(movementRequest.getAliasOrigin())
                || nonNull(movementRequest.getEmailOrigin()));
        result = result && (movementRequest.getAmount() > 0);
        result = result && (nonNull(coinTypeDataService.getByNameAbbr(movementRequest.getCoinTypeNameAbbr())));

        return result;
    }

    public void applyMovement(MovementRequest movementRequest) throws CreateEntityException {
        CoinType coinType = coinTypeDataService.getByNameAbbr(movementRequest.getCoinTypeNameAbbr())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Coin Type not found for name: %s", movementRequest.getCoinTypeNameAbbr())));

        if (nonNull(movementRequest.getUserIdOrigin())) {
            processByUserId(movementRequest, coinType);
        } else if (nonNull(movementRequest.getAliasOrigin())) {
            processByAlias(movementRequest, coinType);
        } else if (nonNull(movementRequest.getEmailOrigin())) {
            processByEmail(movementRequest, coinType);
        }
    }

    void processByUserId(MovementRequest movementRequest, CoinType coinType) throws CreateEntityException {
        User user = userService.getById(movementRequest.getUserIdOrigin());

        validateAlias(movementRequest);
        validateEmail(movementRequest);

        Wallet wallet = getWallet(coinType, user);
        performExtract(movementRequest, wallet);
        persistMovement(wallet, coinType, movementRequest.getAmount());
    }

    void processByAlias(MovementRequest movementRequest, CoinType coinType) throws CreateEntityException {
        User user = userService.getByAlias(movementRequest.getAliasOrigin());

        validateEmail(movementRequest);

        Wallet wallet = getWallet(coinType, user);
        performExtract(movementRequest, wallet);
        persistMovement(wallet, coinType, movementRequest.getAmount());

    }

    void processByEmail(MovementRequest movementRequest, CoinType coinType) throws CreateEntityException {
        User user = userService.getByEmail(movementRequest.getEmailOrigin());

        Wallet wallet = getWallet(coinType, user);

        performExtract(movementRequest, wallet);
        persistMovement(wallet, coinType, movementRequest.getAmount());
    }


    private void performExtract(MovementRequest movementRequest, Wallet wallet) {
        Double newBalance = wallet.getBalance() - movementRequest.getAmount();
        if (newBalance >= 0) {
            walletDataService.updateWalletBalance(wallet.getId(), newBalance);
        } else {
            throw new InsufficientFundsException(String.format("Insufficient Funds to perform extraction. Amount: %s - Balance: %s", movementRequest, wallet.getBalance()));
        }
    }

    private void persistMovement(Wallet walletOrigin, CoinType coinType, Double amount) throws CreateEntityException {
        Movement movement = Movement.builder()
                .coinTypeId(coinType.getId())
                .movementType(extract)
                .originWalletId(walletOrigin.getId())
                .amount(amount)
                .build();
        movementDataService.create(movement);
    }

}
