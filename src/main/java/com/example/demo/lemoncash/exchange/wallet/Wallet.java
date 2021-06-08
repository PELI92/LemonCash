package com.example.demo.lemoncash.exchange.wallet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
public class Wallet {
    @EqualsAndHashCode.Exclude
    private Long id;
    private String coinName;
    private String formatedBalance;
    private Integer coinTypeId;

    @JsonIgnore
    private Double balance;

}