package com.spdu.bll.services;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.dal.repository.ChatRepository;
import com.spdu.domain_models.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Optional<Chat> getById(long id) {
        return chatRepository.getById(id);
    }

    @Override
    public Optional<Chat> create(Chat chat) {
        long chatId = chatRepository.create(chat);
        joinToChat(chat.getOwnerId(), chatId);
        return getById(chatId);
    }

    @Override
    public long joinToChat(long userId, long chatId) {
        if (userIsPresentInChat(userId, chatId)) {
            return 0;
        }
        return chatRepository.joinToChat(userId, chatId);
    }

    @Override
    public boolean userIsPresentInChat(long userId, long chatId) {
        return chatRepository.userIsPresentInChat(userId, chatId);
    }

    @Override
    public List<Chat> getAll(long userId) {
        return chatRepository.getAll(userId);
    }

    @Override
    public List<Chat> getAllOwn(long userId) {
        return chatRepository.getAllOwn(userId);
    }

    @Override
    public List<Chat> getPublic(long userId) {
        return chatRepository.getPublic(userId);
    }
}
