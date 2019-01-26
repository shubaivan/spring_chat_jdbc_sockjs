package com.spdu.dal.repository;

import com.spdu.model.entities.Message;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    Optional<Message> getById(long id) throws SQLException;

    List<Message> getByChatId(long id) throws SQLException;

    List<Message> getAllMessages() throws SQLException;

    long create(Message message) throws SQLException;
}
