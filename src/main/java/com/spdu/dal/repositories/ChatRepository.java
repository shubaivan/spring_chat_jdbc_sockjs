package com.spdu.dal.repositories;

import com.spdu.bll.custom_exceptions.ChatException;
import com.spdu.domain_models.entities.Chat;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ChatRepository {

    Optional<Chat> getById(long id) throws EmptyResultDataAccessException;

    long create(Chat chat);

    long joinToChat(long userId, long chatId);

    Chat update(long id, Chat chat) throws ChatException;

    List<Chat> getAll(long userId);

    List<Chat> getAllPrivate(long userId);

    List<Chat> getPublic(long userId);

    List<Chat> getAllOwn(long userId);

    boolean userIsPresentInChat(long userId, long chatId) throws EmptyResultDataAccessException;

    List<Chat> userIsPresentInOwnerPrivateChat(
            long ownerId,
            long appendUserId);

    int removeChatUser(long userId, long chatId);

    int removeChat(long chatId);

    boolean isOwnChat(long chatId, long userId);
}
