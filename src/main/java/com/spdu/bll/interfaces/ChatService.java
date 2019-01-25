package com.spdu.bll.interfaces;

import com.spdu.model.entities.Chat;

import java.util.Optional;

public interface ChatService {
    Optional<Chat> getById(long id);

    Optional<Chat> create(Chat chat);

    long joinToChat(long userId, long chatId);
}
