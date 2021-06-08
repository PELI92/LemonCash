package com.example.demo.lemoncash.exchange.movement.strategy;

import com.example.demo.lemoncash.exceptions.BadRequestException;
import com.example.demo.lemoncash.exceptions.RetrieveEntityException;
import com.example.demo.lemoncash.exchange.coin.type.CoinType;
import com.example.demo.lemoncash.exchange.coin.type.CoinTypeDataService;
import com.example.demo.lemoncash.exchange.movement.MovementDataService;
import com.example.demo.lemoncash.exchange.movement.MovementRequest;
import com.example.demo.lemoncash.exchange.user.User;
import com.example.demo.lemoncash.exchange.wallet.Wallet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;

@Service
public abstract class MovementResolver {

    @Resource
    protected MovementDataService movementDataService;

    @Resource
    protected CoinTypeDataService coinTypeDataService;

    public abstract boolean isValidMovement(MovementRequest movementRequest);

    public abstract void applyMovement(MovementRequest movementRequest) throws SQLException;

    abstract void processByUserId(MovementRequest movementRequest, CoinType coinType) throws SQLException;

    abstract void processByAlias(MovementRequest movementRequest, CoinType coinType) throws SQLException;

    abstract void processByEmail(MovementRequest movementRequest, CoinType coinType) throws SQLException;

    void validateAlias(MovementRequest movementRequest) {
        if (StringUtils.isNotEmpty(movementRequest.getAliasOrigin())) {
            throw new BadRequestException(String.format("Inconsistent request for user alias: %s, should not have alias", movementRequest));
        }
    }

    void validateEmail(MovementRequest movementRequest) {
        if (StringUtils.isNotEmpty(movementRequest.getEmailOrigin())) {
            throw new BadRequestException(String.format("Inconsistent request for user email: %s, should not have email", movementRequest));
        }
    }

    Wallet getWallet(CoinType coinType, User user) {
        return user.getWallets().stream()
                .filter(w -> w.getCoinTypeId().equals(coinType.getId()))
                .findFirst()
                .orElseThrow(() -> new RetrieveEntityException(String.format("Wallet not found for user: %s and coin type: %s", user, coinType.getNameAbbr())));
    }

}
