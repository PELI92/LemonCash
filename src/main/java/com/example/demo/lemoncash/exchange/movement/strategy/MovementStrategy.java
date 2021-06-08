package com.example.demo.lemoncash.exchange.movement.strategy;

import com.example.demo.lemoncash.exchange.movement.MovementType;
import lombok.Builder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.security.InvalidParameterException;


@Builder
@Service
public class MovementStrategy {

    @NotNull
    private final MovementResolverDeposit movementDepositResolver;
    @NotNull
    private final MovementResolverExtract movementExtractResolver;
    @NotNull
    private final MovementResolverTransfer movementTransactionResolver;

    public MovementResolver getMovementResolver(MovementType movementType) throws InvalidParameterException {
        switch (movementType) {
            case DEPOSIT:
                return movementDepositResolver;
            case EXTRACT:
                return movementExtractResolver;
            case TRANSFER:
                return movementTransactionResolver;
            default:
                throw new InvalidParameterException();
        }
    }
}
