package com.example.demo.lemoncash.exchange.wallet;

import com.example.demo.lemoncash.exchange.coin.type.CoinType;

import java.util.LinkedList;
import java.util.List;

public class DefaultWalletFactory {
    public static List<Wallet> defaultEmptyWallet(List<CoinType> coinTypes) {
        List<Wallet> wallets = new LinkedList<>();
        for (CoinType ct : coinTypes) {
            Wallet wallet = Wallet.builder()
                    .coinTypeId(ct.getId())
                    .balance(0.0)
                    .build();
            wallets.add(wallet);
        }
        return wallets;
    }
}
