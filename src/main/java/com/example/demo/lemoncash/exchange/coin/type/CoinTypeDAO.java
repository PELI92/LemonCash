package com.example.demo.lemoncash.exchange.coin.type;

import com.example.demo.lemoncash.exceptions.RetrieveEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CoinTypeDAO {

    @Resource
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String QUERY_GET_BY_ID = "SELECT * FROM coin_type";

    public List<CoinType> getAllCoinTypes() throws RetrieveEntityException {
        return namedParameterJdbcTemplate.query(QUERY_GET_BY_ID, new CoinTypeMapper());
    }
}
