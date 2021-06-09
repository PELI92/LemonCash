package com.example.demo.lemoncash.exchange.user;

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

import static com.example.demo.lemoncash.config.Status.ACTIVE;
import static com.example.demo.lemoncash.exchange.user.UserDataValidationService.checkAttributesForInsert;

@Service
@RequiredArgsConstructor
public class UserDAO {

    @Resource
    private final DateService dateService;

    @Resource
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Resource
    private final JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert simpleJdbcInsert;

    private final static String QUERY_GET_BY_ID = "SELECT * FROM USER WHERE user_id = :user_id";
    private final static String QUERY_GET_BY_ALIAS = "SELECT * FROM USER WHERE alias = :alias";
    private final static String QUERY_GET_BY_EMAIL = "SELECT * FROM USER WHERE email = :email";

    @PostConstruct
    public void init() {
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USER")
                .usingGeneratedKeyColumns("USER_ID");
    }

    public Long save(User user) throws CreateEntityException {
        checkAttributesForInsert(user);
        try {
            SqlParameterSource parameters = new MapSqlParameterSource()
                    .addValue("NAME", user.getName())
                    .addValue("SURNAME", user.getSurname())
                    .addValue("ALIAS", user.getAlias())
                    .addValue("EMAIL", user.getEmail())
                    .addValue("STATUS", ACTIVE.getValue())
                    .addValue("LAST_UPDATED", dateService.getNowDate());

            return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        } catch (DataAccessException dae) {
            throw new CreateEntityException(dae.getMessage(), dae);
        }
    }

    public List<User> getById(Long id) throws RetrieveEntityException {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("user_id", id);
        return namedParameterJdbcTemplate.query(QUERY_GET_BY_ID, mapSqlParameterSource, new UserMapper());
    }

    public List<User> getByAlias(String alias) throws RetrieveEntityException {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("alias", alias);
        return namedParameterJdbcTemplate.query(QUERY_GET_BY_ALIAS, mapSqlParameterSource, new UserMapper());
    }

    public List<User> getByEmail(String email) throws RetrieveEntityException {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("email", email);
        return namedParameterJdbcTemplate.query(QUERY_GET_BY_EMAIL, mapSqlParameterSource, new UserMapper());
    }

}
