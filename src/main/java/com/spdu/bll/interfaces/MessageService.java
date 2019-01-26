package com.spdu.bll.interfaces;

import com.spdu.model.entities.Message;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MessageService {
    Optional<Message> getById(long id);

    List<Message> getByChatId(long id) throws SQLException;

    List<Message> getAllMessages() throws SQLException;

    Optional<Message> create(Message message);
}
