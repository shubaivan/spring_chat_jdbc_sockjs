package com.spdu.bll.interfaces;

import com.spdu.bll.custom_exceptions.ChatException;
import com.spdu.bll.models.ChatDto;
import com.spdu.domain_models.entities.Chat;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ChatService {
    Optional<Chat> getById(long id);

    Chat create(ChatDto chatDto);

    ChatDto update(long id, ChatDto chatDto) throws ChatException;

    boolean joinToChat(long userId, long chatId);

    List<Chat> getAll(long userId);

    List<Chat> getAllOwn(long userId);

    List<Chat> getPublic(long userId);

    List<Chat> getPrivate(long userId);

    boolean userIsPresentInChat(long userId, long chatId);

    boolean removeChat(long chatId, long ownerId) throws ChatException;
}
