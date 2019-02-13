package com.spdu.dal.mappers;

import com.spdu.domain_models.entities.ConfirmationToken;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfirmationTokenMapper implements RowMapper<ConfirmationToken> {

    @Override
    public ConfirmationToken mapRow(ResultSet rs, int rowNum) throws SQLException {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setConfirmationToken(rs.getString("confirmation_token"));
        confirmationToken.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        confirmationToken.setUserId(rs.getLong("user_id"));
        confirmationToken.setId(rs.getLong("id"));
        return confirmationToken;
    }
}
