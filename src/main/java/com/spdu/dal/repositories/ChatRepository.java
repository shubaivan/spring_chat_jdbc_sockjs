package com.spdu.dal.repositories;

import com.spdu.domain_models.entities.Chat;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {

    Optional<Chat> getById(long id) throws EmptyResultDataAccessException;

    long create(Chat chat) throws SQLException;

    long joinToChat(long userId, long chatId);

    List<Chat> getAll(long userId) throws EmptyResultDataAccessException;

    List<Chat> getPublic(long userId) throws EmptyResultDataAccessException;

    List<Chat> getAllOwn(long userId) throws EmptyResultDataAccessException;

    boolean userIsPresentInChat(long userId, long chatId) throws EmptyResultDataAccessException;
}
