package com.example.demo.lemoncash.exchange.wallet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
public class Wallet {
    @EqualsAndHashCode.Exclude
    private Long id;
    private String coinName;
    @JsonProperty("balance")
    private String formatedBalance;

    @JsonIgnore
    private Integer coinTypeId;
    @JsonIgnore
    private Double balance;

}