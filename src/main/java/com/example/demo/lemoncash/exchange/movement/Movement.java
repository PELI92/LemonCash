package com.example.demo.lemoncash.exchange.movement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Movement {
    private Long id;
    private MovementType movementType;
    private String cointTypeNameAbbr;
    private Long originWalletId;
    private Long destinationWalletId;
    private Double amount;

    @JsonIgnore
    private Integer coinTypeId;
}