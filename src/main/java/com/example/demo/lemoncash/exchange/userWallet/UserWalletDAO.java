package com.example.demo.lemoncash.exchange.userWallet;

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

import static com.example.demo.lemoncash.exchange.userWallet.UserWalletDataValidationService.checkAttributesForInsert;


@Service
@RequiredArgsConstructor
public class UserWalletDAO {

    @Resource
    private final DateService dateService;

    @Resource
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Resource
    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert simpleJdbcInsert;

    private final static String QUERY_GET_BY_USER_ID = "SELECT * FROM user_wallet WHERE user_id = :user_id";
    private final static String QUERY_GET_BY_WALLET_ID = "SELECT * FROM user_wallet WHERE wallet_id = :wallet_id";

    @PostConstruct
    public void init() {
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USER_WALLET")
                .usingGeneratedKeyColumns("USER_WALLET_ID");
    }

    public Long save(UserWallet userWallet) throws CreateEntityException {
        checkAttributesForInsert(userWallet);
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("USER_ID", userWallet.getUserId())
                    .addValue("WALLET_ID", userWallet.getWalletId())
                    .addValue("LAST_UPDATED", dateService.getNowDate());

            return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        } catch (DataAccessException dae) {
            throw new CreateEntityException(dae.getMessage(), dae);
        }
    }

    public List<UserWallet> getByUserId(Long userId) throws RetrieveEntityException {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("user_id", userId);
        return namedParameterJdbcTemplate.query(QUERY_GET_BY_USER_ID, mapSqlParameterSource, new UserWalletMapper());
    }

    public List<UserWallet> getByWalletId(Long walletId) throws RetrieveEntityException {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("wallet_id", walletId);
        return namedParameterJdbcTemplate.query(QUERY_GET_BY_WALLET_ID, mapSqlParameterSource, new UserWalletMapper());
    }
}
