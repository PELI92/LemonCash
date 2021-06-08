package com.example.demo.lemoncash.exchange.user;

import com.example.demo.lemoncash.exchange.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class UserMapper extends Mapper<User> {

    @Override
    public User mapRow(ResultSet rs, int i) throws SQLException {
        return User.builder()
                .id(rs.getLong("USER_ID"))
                .name(rs.getString("NAME"))
                .surname(rs.getString("SURNAME"))
                .alias(rs.getString("ALIAS"))
                .email(rs.getString("EMAIL"))
                .wallets(new LinkedList<>())
                .build();
    }
}
