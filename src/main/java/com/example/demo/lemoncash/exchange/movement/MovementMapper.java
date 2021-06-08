package com.example.demo.lemoncash.exchange.movement;

import com.example.demo.lemoncash.exchange.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovementMapper extends Mapper<Movement> {

    @Override
    public Movement mapRow(ResultSet rs, int i) throws SQLException {
        Long originWalletId = (rs.getLong("ORIGIN_WALLET_ID") !=0L)? rs.getLong("ORIGIN_WALLET_ID"): null;
        Long destinationWalletId = (rs.getLong("DESTINATION_WALLET_ID") !=0L)? rs.getLong("DESTINATION_WALLET_ID"): null;
        return Movement.builder()
                .id(rs.getLong("MOVEMENT_ID"))
                .movementType(MovementType.valueOf(rs.getString("MOVEMENT_TYPE")))
                .originWalletId(originWalletId)
                .destinationWalletId(destinationWalletId)
                .coinTypeId(rs.getInt("COIN_TYPE_ID"))
                .amount(rs.getDouble("AMOUNT"))
                .build();
    }
}
