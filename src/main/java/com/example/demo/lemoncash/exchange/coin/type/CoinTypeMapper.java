package com.example.demo.lemoncash.exchange.coin.type;

import com.example.demo.lemoncash.exchange.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CoinTypeMapper extends Mapper<CoinType> {

    @Override
    public CoinType mapRow(ResultSet rs, int i) throws SQLException {
        return CoinType.builder()
                .id(rs.getInt("COIN_TYPE_ID"))
                .name(rs.getString("NAME"))
                .nameAbbr(rs.getString("NAME_ABBR"))
                .format(rs.getString("FORMAT"))
                .build();
    }
}
