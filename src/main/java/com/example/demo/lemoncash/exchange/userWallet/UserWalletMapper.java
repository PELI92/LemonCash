package com.example.demo.lemoncash.exchange.userWallet;

import com.example.demo.lemoncash.exchange.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserWalletMapper extends Mapper<UserWallet> {

    @Override
    public UserWallet mapRow(ResultSet rs, int i) throws SQLException {
        return UserWallet.builder()
                .userId((rs.getLong("USER_ID")))
                .walletId((rs.getLong("WALLET_ID")))
                .build();
    }
}
