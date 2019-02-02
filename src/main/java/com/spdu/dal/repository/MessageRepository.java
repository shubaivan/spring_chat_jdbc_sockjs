package com.spdu.dal.repository;

import com.spdu.domain_models.entities.Message;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    Optional<Message> getById(long id);

    List<Message> getByChatId(long id);

    List<Message> getAllMessages();

    long create(Message message);
}
