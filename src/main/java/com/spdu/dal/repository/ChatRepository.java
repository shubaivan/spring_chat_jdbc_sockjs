package com.spdu.dal.repository;

import com.spdu.model.entities.Chat;

import java.sql.SQLException;

public interface ChatRepository {

    Chat getById(long id) throws SQLException;
}
