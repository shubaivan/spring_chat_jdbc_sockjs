package com.spdu.bll.interfaces;

import com.spdu.domain_models.entities.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatService {
    Optional<Chat> getById(long id);

    Optional<Chat> create(Chat chat);

    long joinToChat(long userId, long chatId);

    List<Chat> getAll(long userId);

    List<Chat> getAllOwn(long userId);

    List<Chat> getPublic(long userId);

    boolean userIsPresentInChat(long userId, long chatId);
}
