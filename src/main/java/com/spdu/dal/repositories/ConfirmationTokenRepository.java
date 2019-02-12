package com.spdu.dal.repositories;

import com.spdu.domain_models.entities.ConfirmationToken;

public interface ConfirmationTokenRepository {
    String setConfirmationToken(ConfirmationToken confirmationToken);
}
