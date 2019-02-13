package com.spdu.dal.repositories;

import com.spdu.dal.mappers.ConfirmationTokenMapper;
import com.spdu.domain_models.entities.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Repository
public class ConfirmationTokenRepositoryImpl implements ConfirmationTokenRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ConfirmationTokenRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public String setConfirmationToken(ConfirmationToken confirmationToken) throws SQLException {
        String query = "INSERT INTO confirmation_token (" +
                "confirmation_token, created_at," +
                "user_id) VALUES (?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, confirmationToken.getConfirmationToken());
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(3, confirmationToken.getUserId());
            return ps;
        }, keyHolder);
        return String.valueOf(keyHolder.getKeys().get("confirmation_token").toString());
    }

    @Override
    public ConfirmationToken getConfirmationToken(String token) {
        try {
            String query = "SELECT * FROM confirmation_token WHERE confirmation_token =?";

            ConfirmationToken confirmationToken = jdbcTemplate.queryForObject(query,
                    new Object[]{token},
                    new ConfirmationTokenMapper());
            return confirmationToken;
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeToken(long id) throws SQLException {
        String query = "DELETE FROM confirmation_token WHERE id =?";
        jdbcTemplate.update(query, id);
        return true;
    }
}
