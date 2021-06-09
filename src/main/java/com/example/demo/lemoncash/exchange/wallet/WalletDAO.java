package com.example.demo.lemoncash.exchange.wallet;

import com.example.demo.lemoncash.date.DateService;
import com.example.demo.lemoncash.exceptions.CreateEntityException;
import com.example.demo.lemoncash.exceptions.RetrieveEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

import static com.example.demo.lemoncash.exchange.wallet.WalletDataValidationService.checkAttributesForInsert;


@Service
@RequiredArgsConstructor
public class WalletDAO {

    @Resource
    private final DateService dateService;

    @Resource
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Resource
    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert simpleJdbcInsert;

    private final static String SELECT_BY_WALLET_ID = "SELECT * FROM WALLET WHERE wallet_id = :wallet_id";
    private final static String SELECT_IN_WALLET_ID = "SELECT * FROM WALLET WHERE wallet_id IN (:inValues);";
    private final static String UPDATE_BALANCE_BY_WALLET_ID = "UPDATE WALLET SET balance = :balance WHERE wallet_id = :wallet_id";

    @PostConstruct
    public void init() {
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("WALLET")
                .usingGeneratedKeyColumns("WALLET_ID");
    }

    public Long save(Wallet wallet) throws CreateEntityException {
        checkAttributesForInsert(wallet);
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("BALANCE", wallet.getBalance())
                    .addValue("COIN_TYPE_ID", wallet.getCoinTypeId())
                    .addValue("LAST_UPDATED", dateService.getNowDate());

            return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        } catch (DataAccessException dae) {
            throw new CreateEntityException(dae.getMessage(), dae);
        }
    }

    public List<Wallet> getById(Long walletId) throws RetrieveEntityException {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("wallet_id", walletId);
        return namedParameterJdbcTemplate.query(SELECT_BY_WALLET_ID, mapSqlParameterSource, new WalletMapper());
    }

    public List<Wallet> getByIds(List<Long> walletIds) throws RetrieveEntityException {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("inValues", walletIds);
        return namedParameterJdbcTemplate.query(SELECT_IN_WALLET_ID, mapSqlParameterSource, new WalletMapper());
    }

    public int updateWalletBalance(Long walletId, Double balance) throws RetrieveEntityException {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("balance", balance)
                .addValue("wallet_id", walletId);
        return namedParameterJdbcTemplate.update(UPDATE_BALANCE_BY_WALLET_ID, mapSqlParameterSource);
    }
}
