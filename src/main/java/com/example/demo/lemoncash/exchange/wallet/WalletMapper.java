package com.example.demo.lemoncash.exchange.wallet;

import com.example.demo.lemoncash.exchange.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WalletMapper extends Mapper<Wallet> {

    @Override
    public Wallet mapRow(ResultSet rs, int i) throws SQLException {
        return Wallet.builder()
                .id(rs.getLong("WALLET_ID"))
                .balance(rs.getDouble("BALANCE"))
                .coinTypeId(rs.getInt("COIN_TYPE_ID"))
                .build();
    }
}
