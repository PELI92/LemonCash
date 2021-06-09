package com.example.demo.lemoncash.exchange.movement;

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

import static com.example.demo.lemoncash.exchange.movement.MovementDataValidationService.checkAttributesForInsert;
import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class MovementDAO {

    @Resource
    private final DateService dateService;

    @Resource
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Resource
    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert simpleJdbcInsert;

    private final static String LIMIT = " LIMIT %s";
    private final static String OFFSET = " OFFSET %s";

    private final static String QUERY_GET_BY_WALLET_ID = "SELECT * FROM MOVEMENT WHERE origin_wallet_id = :wallet_id OR destination_wallet_id = :wallet_id ORDER BY movement_id";
    private final static String QUERY_GET_BY_WALLET_ID_AND_MOVEMENT_TYPE = "SELECT * FROM MOVEMENT WHERE (origin_wallet_id = :wallet_id OR destination_wallet_id = :wallet_id) AND movement_type = :movement_type ORDER BY movement_id";
    private final static String QUERY_GET_IN_WALLET_ID = "SELECT * FROM MOVEMENT WHERE origin_wallet_id IN (:wallet_ids) OR destination_wallet_id IN (:wallet_ids) ORDER BY movement_id";
    private final static String QUERY_GET_IN_WALLET_ID_AND_MOVEMENT_TYPE = "SELECT * FROM MOVEMENT WHERE (origin_wallet_id IN (:wallet_ids) OR destination_wallet_id IN (:wallet_ids)) AND movement_type = :movement_type ORDER BY movement_id";

    @PostConstruct
    public void init() {
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("MOVEMENT")
                .usingGeneratedKeyColumns("MOVEMENT_ID");
    }

    public Long save(Movement movement) throws CreateEntityException {
        checkAttributesForInsert(movement);
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("ORIGIN_WALLET_ID", movement.getOriginWalletId())
                    .addValue("DESTINATION_WALLET_ID", movement.getDestinationWalletId())
                    .addValue("MOVEMENT_TYPE", movement.getMovementType())
                    .addValue("COIN_TYPE_ID", movement.getCoinTypeId())
                    .addValue("AMOUNT", movement.getAmount())
                    .addValue("LAST_UPDATED", dateService.getNowDate());

            return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        } catch (DataAccessException dae) {
            throw new CreateEntityException(dae.getMessage(), dae);
        }
    }

    public List<Movement> getByWalletId(Long walletId, Integer limit, Integer offset) throws RetrieveEntityException {

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("wallet_id", walletId);
        return namedParameterJdbcTemplate.query(getQuery(QUERY_GET_BY_WALLET_ID, limit, offset), mapSqlParameterSource, new MovementMapper());
    }

    public List<Movement> getByWalletIdAndMovementType(Long walletId, MovementType movementType, Integer limit, Integer offset) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("wallet_id", walletId)
                .addValue("movement_type", movementType);
        return namedParameterJdbcTemplate.query(getQuery(QUERY_GET_BY_WALLET_ID_AND_MOVEMENT_TYPE, limit, offset), mapSqlParameterSource, new MovementMapper());
    }

    public List<Movement> getByWalletIds(List<Long> walletIds, Integer limit, Integer offset) throws RetrieveEntityException {

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("wallet_ids", walletIds);
        return namedParameterJdbcTemplate.query(getQuery(QUERY_GET_IN_WALLET_ID, limit, offset), mapSqlParameterSource, new MovementMapper());
    }

    public List<Movement> getByWalletIdsAndMovementType(List<Long> walletIds, MovementType movementType, Integer limit, Integer offset) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("wallet_ids", walletIds)
                .addValue("movement_type", movementType.name());
        return namedParameterJdbcTemplate.query(getQuery(QUERY_GET_IN_WALLET_ID_AND_MOVEMENT_TYPE, limit, offset), mapSqlParameterSource, new MovementMapper());
    }

    private String getQuery(String query, Integer limit, Integer offset) {
        if (nonNull(limit)) {
            query = query.concat(String.format(LIMIT, limit));
        }
        if (nonNull(offset)) {
            query = query.concat(String.format(OFFSET, offset));
        }
        return query;
    }

}
