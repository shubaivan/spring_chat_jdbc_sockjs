package com.spdu.dal.repository;

import com.spdu.model.entities.Chat;

import java.sql.SQLException;
import java.util.Optional;

public interface ChatRepository {

    Optional<Chat> getById(long id) throws SQLException;

    long create(Chat chat) throws SQLException;

    long joinToChat(long userId, long chatId) throws SQLException;
}
