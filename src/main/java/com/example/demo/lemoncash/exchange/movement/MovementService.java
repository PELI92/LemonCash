package com.example.demo.lemoncash.exchange.movement;

import com.example.demo.lemoncash.exceptions.BadRequestException;
import com.example.demo.lemoncash.exchange.coin.type.CoinType;
import com.example.demo.lemoncash.exchange.coin.type.CoinTypeDataService;
import com.example.demo.lemoncash.exchange.movement.strategy.MovementResolver;
import com.example.demo.lemoncash.exchange.movement.strategy.MovementStrategy;
import com.example.demo.lemoncash.exchange.userWallet.UserWallet;
import com.example.demo.lemoncash.exchange.userWallet.UserWalletDataService;
import com.example.demo.lemoncash.exchange.wallet.Wallet;
import com.example.demo.lemoncash.exchange.wallet.WalletDataService;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;

@Service
@Builder
public class MovementService {

    private final MovementStrategy movementStrategy;

    private final MovementDataService movementDataService;

    private final UserWalletDataService userWalletDataService;

    private final WalletDataService walletDataService;

    private final CoinTypeDataService coinTypeDataService;

    void applyMovement(MovementRequest movementRequest) throws SQLException {
        MovementResolver movementResolver = movementStrategy.getMovementResolver(movementRequest.getMovementType());
        if (movementResolver.isValidMovement(movementRequest)) {
            movementResolver.applyMovement(movementRequest);
        } else {
            throw new BadRequestException("Invalid or insufficient information to apply movement");
        }
    }

    List<Movement> listMovementsByUserId(Long userId, String coinTypeNameAbbr, MovementType movementType, Integer limit, Integer offset) {
        Optional<CoinType> optionalCoinType = StringUtils.isNotEmpty(coinTypeNameAbbr) ? coinTypeDataService.getByNameAbbr(coinTypeNameAbbr) : empty();
        Optional<MovementType> optionalMovementType = Optional.ofNullable(movementType);

        List<UserWallet> userWallets = userWalletDataService.getByUserId(userId);

        List<Long> walletIds = userWallets.stream()
                .map(UserWallet::getWalletId)
                .collect(toList());

        List<Wallet> walletList = walletDataService.getByIds(walletIds);
        if (optionalCoinType.isPresent()) {
            walletIds = filterCoinType(walletList, optionalCoinType.get()).stream()
                    .map(Wallet::getId)
                    .collect(toList());
        }

        return optionalMovementType.isPresent()
                ? movementDataService.getByWalletIdsAndMovementType(walletIds, optionalMovementType.get(), limit, offset)
                : movementDataService.getByWalletIds(walletIds, limit, offset);

    }

    List<Wallet> filterCoinType(List<Wallet> walletList, CoinType coinType) {
        return walletList.stream()
                .filter(w -> coinType.getId().equals(w.getCoinTypeId()))
                .collect(toList());

    }

}
