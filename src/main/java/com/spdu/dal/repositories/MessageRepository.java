package com.spdu.dal.repositories;

import com.spdu.domain_models.entities.Message;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    Optional<Message> getById(long id) throws EmptyResultDataAccessException;

//    List<Message> getByChatId(long id) throws EmptyResultDataAccessException;

    long create(Message message) throws SQLException;

//    List<Message> searchMessages(long id, String keyword) throws EmptyResultDataAccessException;

    List<Message> getMessages(long id, Optional<String> keyword) throws EmptyResultDataAccessException;
}
