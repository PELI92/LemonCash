package com.example.demo.lemoncash.exchange.movement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovementResponse {
    private Long id;
    private MovementType movementType;
    @JsonProperty("coin_type")
    private String coinTypeNameAbbr;
    private Long originWalletId;
    private Long destinationWalletId;
    private String amount;

    @JsonIgnore
    private Integer coinTypeId;
}