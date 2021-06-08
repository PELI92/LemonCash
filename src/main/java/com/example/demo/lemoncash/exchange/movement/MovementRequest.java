package com.example.demo.lemoncash.exchange.movement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MovementRequest {

    @NonNull
    private MovementType movementType;
    @NonNull
    @JsonProperty("coin_type")
    private String coinTypeNameAbbr;
    @NonNull
    private Double amount;

    private Long userIdOrigin;
    private Long userIdDestination;

    private String aliasOrigin;
    private String aliasDestination;

    private String emailOrigin;
    private String emailDestination;

}
