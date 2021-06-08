package com.example.demo.lemoncash.fixture;

import com.example.demo.lemoncash.exchange.coin.type.CoinType;

import java.util.List;

public class CoinTypeFixture {

    public static List<CoinType> getAllCoinTypes() {
        return List.of(
                CoinType.builder()
                        .id(1)
                        .name("ARS")
                        .nameAbbr("Peso Argentino")
                        .format("0.00")
                        .build(),
                CoinType.builder()
                        .id(2)
                        .name("USDT")
                        .nameAbbr("Tether")
                        .format("0.00")
                        .build(),
                CoinType.builder()
                        .id(3)
                        .name("BTC")
                        .nameAbbr("Bitcoin")
                        .format("0.00")
                        .build()
        );
    }
}
