package com.spdu.dal.repositories;

import com.spdu.domain_models.entities.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
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
    public long setConfirmationToken(ConfirmationToken confirmationToken) {
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
        return Long.valueOf(keyHolder.getKeys().get("id").toString());
    }
}
