package com.example.demo.lemoncash.exchange.movement.strategy;

import com.example.demo.lemoncash.database.TransactionService;
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

import java.sql.SQLException;

import static com.example.demo.lemoncash.exchange.movement.MovementType.transfer;
import static java.util.Objects.nonNull;

@Builder
@Service
public class MovementResolverTransfer extends MovementResolver {

    private final UserService userService;
    private final UserWalletDataService userWalletDataService;
    private final WalletDataService walletDataService;
    private final TransactionService transactionService;

    public boolean isValidMovement(MovementRequest movementRequest) {
        boolean result = (
                   (nonNull(movementRequest.getUserIdOrigin()) && nonNull(movementRequest.getUserIdDestination()))
                || (nonNull(movementRequest.getAliasOrigin()) && nonNull(movementRequest.getAliasDestination()))
                || (nonNull(movementRequest.getEmailOrigin()) && nonNull(movementRequest.getEmailDestination()))
        );
        result = result && (movementRequest.getAmount() > 0);
        result = result && (nonNull(coinTypeDataService.getByNameAbbr(movementRequest.getCoinTypeNameAbbr())));

        return result;
    }

    public void applyMovement(MovementRequest movementRequest) throws SQLException {
        CoinType coinType = coinTypeDataService.getByNameAbbr(movementRequest.getCoinTypeNameAbbr())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Coin Type not found for name: %s", movementRequest.getCoinTypeNameAbbr())));

        if (nonNull(movementRequest.getUserIdOrigin()) && nonNull(movementRequest.getUserIdDestination())) {
            processByUserId(movementRequest, coinType);
        } else if (nonNull(movementRequest.getAliasOrigin()) && nonNull(movementRequest.getAliasDestination())) {
            processByAlias(movementRequest, coinType);
        } else if (nonNull(movementRequest.getEmailOrigin()) && nonNull(movementRequest.getEmailDestination())) {
            processByEmail(movementRequest, coinType);
        }
    }

    void processByUserId(MovementRequest movementRequest, CoinType coinType) throws SQLException {
        User userOrigin = userService.getById(movementRequest.getUserIdOrigin());
        User userDestination = userService.getById(movementRequest.getUserIdDestination());

        validateAlias(movementRequest);
        validateEmail(movementRequest);

        Wallet walletOrigin = getWallet(coinType, userOrigin);
        Wallet walletDestination = getWallet(coinType, userDestination);

        performTransfer(movementRequest, walletOrigin, walletDestination);
        persistMovement(walletOrigin, walletDestination, coinType, movementRequest.getAmount());
    }

    void processByAlias(MovementRequest movementRequest, CoinType coinType) throws SQLException {
        User userOrigin = userService.getByAlias(movementRequest.getAliasOrigin());
        User userDestination = userService.getByAlias(movementRequest.getAliasDestination());

        validateEmail(movementRequest);

        Wallet walletOrigin = getWallet(coinType, userOrigin);
        Wallet walletDestination = getWallet(coinType, userDestination);

        performTransfer(movementRequest, walletOrigin, walletDestination);
        persistMovement(walletOrigin, walletDestination, coinType, movementRequest.getAmount());
    }

    void processByEmail(MovementRequest movementRequest, CoinType coinType) throws SQLException {
        User userOrigin = userService.getByEmail(movementRequest.getEmailOrigin());
        User userDestination = userService.getByEmail(movementRequest.getEmailDestination());

        Wallet walletOrigin = getWallet(coinType, userOrigin);
        Wallet walletDestination = getWallet(coinType, userDestination);

        performTransfer(movementRequest, walletOrigin, walletDestination);
        persistMovement(walletOrigin, walletDestination, coinType, movementRequest.getAmount());
    }


    private void performTransfer(MovementRequest movementRequest, Wallet walletOrigin, Wallet walletDestination) throws SQLException {
        Double newBalanceOrigin = walletOrigin.getBalance() - movementRequest.getAmount();
        Double newBalanceDestination = walletDestination.getBalance() + movementRequest.getAmount();

        if (newBalanceOrigin >= 0) {
            transactionService.executeTransaction(() -> {
                walletDataService.updateWalletBalance(walletOrigin.getId(), newBalanceOrigin);
                walletDataService.updateWalletBalance(walletDestination.getId(), newBalanceDestination);
            });
        } else {
            throw new InsufficientFundsException(String.format("Insufficient Funds to perform extraction. Amount: %s - Balance: %s", movementRequest, walletOrigin.getBalance()));
        }
    }

    private void persistMovement(Wallet walletOrigin, Wallet walletDestination, CoinType coinType, Double amount) {
        Movement movement = Movement.builder()
                .coinTypeId(coinType.getId())
                .movementType(transfer)
                .originWalletId(walletOrigin.getId())
                .destinationWalletId(walletDestination.getId())
                .amount(amount)
                .build();
        movementDataService.create(movement);
    }

}
