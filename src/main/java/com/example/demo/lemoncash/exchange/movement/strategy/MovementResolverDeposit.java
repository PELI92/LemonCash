package com.example.demo.lemoncash.exchange.movement.strategy;

import com.example.demo.lemoncash.exceptions.CreateEntityException;
import com.example.demo.lemoncash.exceptions.EntityNotFoundException;
import com.example.demo.lemoncash.exchange.coin.type.CoinType;
import com.example.demo.lemoncash.exchange.movement.Movement;
import com.example.demo.lemoncash.exchange.movement.MovementRequest;
import com.example.demo.lemoncash.exchange.user.User;
import com.example.demo.lemoncash.exchange.user.UserService;
import com.example.demo.lemoncash.exchange.userWallet.UserWalletDataService;
import com.example.demo.lemoncash.exchange.wallet.Wallet;
import com.example.demo.lemoncash.exchange.wallet.WalletDataService;
import lombok.Builder;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import static com.example.demo.lemoncash.exchange.movement.MovementType.deposit;
import static java.util.Objects.nonNull;

@Builder
@Service
public class MovementResolverDeposit extends MovementResolver {

    private final UserService userService;
    private final UserWalletDataService userWalletDataService;
    private final WalletDataService walletDataService;

    public boolean isValidMovement(MovementRequest movementRequest) {
        boolean result = (nonNull(movementRequest.getUserIdDestination())
                || nonNull(movementRequest.getAliasDestination())
                || nonNull(movementRequest.getEmailDestination()));
        result = result && (movementRequest.getAmount() > 0);
        result = result && (nonNull(coinTypeDataService.getByNameAbbr(movementRequest.getCoinTypeNameAbbr())));

        return result;
    }

    @SneakyThrows
    public void applyMovement(MovementRequest movementRequest) {
        CoinType coinType = coinTypeDataService.getByNameAbbr(movementRequest.getCoinTypeNameAbbr())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Coin Type not found for name: %s", movementRequest.getCoinTypeNameAbbr())));

        if (nonNull(movementRequest.getUserIdDestination())) {
            processByUserId(movementRequest, coinType);
        } else if (nonNull(movementRequest.getAliasDestination())) {
            processByAlias(movementRequest, coinType);
        } else if (nonNull(movementRequest.getEmailDestination())) {
            processByEmail(movementRequest, coinType);
        }
    }


    void processByUserId(MovementRequest movementRequest, CoinType coinType) throws CreateEntityException {
        User user = userService.getById(movementRequest.getUserIdDestination());

        validateAlias(movementRequest);
        validateEmail(movementRequest);

        Wallet wallet = getWallet(coinType, user);

        performDeposit(movementRequest, wallet);
        persistMovement(wallet, coinType, movementRequest.getAmount());
    }

    void processByAlias(MovementRequest movementRequest, CoinType coinType) throws CreateEntityException {
        User user = userService.getByAlias(movementRequest.getAliasDestination());

        validateEmail(movementRequest);

        Wallet wallet = getWallet(coinType, user);

        performDeposit(movementRequest, wallet);
        persistMovement(wallet, coinType, movementRequest.getAmount());
    }

    void processByEmail(MovementRequest movementRequest, CoinType coinType) throws CreateEntityException {
        User user = userService.getByEmail(movementRequest.getEmailDestination());

        Wallet wallet = getWallet(coinType, user);

        performDeposit(movementRequest, wallet);
        persistMovement(wallet, coinType, movementRequest.getAmount());
    }


    private void performDeposit(MovementRequest movementRequest, Wallet wallet) {
        Double newBalance = wallet.getBalance() + movementRequest.getAmount();
        walletDataService.updateWalletBalance(wallet.getId(), newBalance);
    }

    private void persistMovement(Wallet walletDestination, CoinType coinType, Double amount) throws CreateEntityException {
        Movement movement = Movement.builder()
                .coinTypeId(coinType.getId())
                .movementType(deposit)
                .destinationWalletId(walletDestination.getId())
                .amount(amount)
                .build();
        movementDataService.create(movement);
    }

}