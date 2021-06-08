package com.example.demo.lemoncash.exchange.movement;

import com.example.demo.lemoncash.exceptions.CreateEntityException;
import com.example.demo.lemoncash.exceptions.EntityNotFoundException;
import com.example.demo.lemoncash.exchange.coin.type.CoinType;
import com.example.demo.lemoncash.exchange.coin.type.CoinTypeDataService;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Builder
public class MovementDataService {

    private final MovementDAO movementDAO;

    private final CoinTypeDataService coinTypeDataService;

    public List<Movement> getByWalletId(Long walletId, Integer limit, Integer offset) {
        return movementDAO.getByWalletId(walletId, limit, offset).stream()
                .map(this::setCoinTypeNameAbbr)
                .collect(toList());
    }

    public List<Movement> getByWalletIdAndMovementType(Long walletId, MovementType movementType, Integer limit, Integer offset) {
        return movementDAO.getByWalletIdAndMovementType(walletId, movementType, limit, offset).stream()
                .map(this::setCoinTypeNameAbbr)
                .collect(toList());
    }

    public List<Movement> getByWalletIds(List<Long> walletIds, Integer limit, Integer offset) {
        return movementDAO.getByWalletIds(walletIds, limit, offset).stream()
                .map(this::setCoinTypeNameAbbr)
                .collect(toList());
    }

    public List<Movement> getByWalletIdsAndMovementType(List<Long> walletIds, MovementType movementType, Integer limit, Integer offset) {
        return movementDAO.getByWalletIdsAndMovementType(walletIds, movementType, limit, offset).stream()
                .map(this::setCoinTypeNameAbbr)
                .collect(toList());
    }

    public Long create(Movement movement) throws CreateEntityException {
        return movementDAO.save(movement);
    }

    private Movement setCoinTypeNameAbbr(Movement movement) {
        CoinType coinType = coinTypeDataService.getById(movement.getCoinTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Coin Type Not found"));
        movement.setCointTypeNameAbbr(coinType.getNameAbbr());
        return movement;
    }

}
