package com.spdu.dal.repositories;

import com.spdu.domain_models.entities.ConfirmationToken;

import java.sql.SQLException;

public interface ConfirmationTokenRepository {
    String setConfirmationToken(ConfirmationToken confirmationToken) throws SQLException;

    ConfirmationToken getConfirmationToken(String token);

    boolean removeToken(long id) throws SQLException;
}
