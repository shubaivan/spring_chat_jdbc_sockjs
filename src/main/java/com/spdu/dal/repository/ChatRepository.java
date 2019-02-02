package com.spdu.dal.repository;

import com.spdu.domain_models.entities.Chat;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {

    Optional<Chat> getById(long id);

    long create(Chat chat);

    long joinToChat(long userId, long chatId);

    List<Chat> getAll(long userId);

    List<Chat> getPublic(long userId);

    List<Chat> getAllOwn(long userId);

    boolean userIsPresentInChat(long userId, long chatId);
}
