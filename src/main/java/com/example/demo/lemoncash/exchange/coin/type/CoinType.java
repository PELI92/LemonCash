package com.example.demo.lemoncash.exchange.coin.type;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CoinType {
    private Integer id;
    private String name;
    private String nameAbbr;
    private String format;
}
