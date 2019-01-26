package com.spdu.bll.services;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.dal.repository.ChatRepository;
import com.spdu.model.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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
        try {
            return chatRepository.getById(id);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Chat> create(Chat chat) {
        try {
            long chatId = chatRepository.create(chat);
            joinToChat(chat.getOwnerId(), chatId);
            return getById(chatId);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public long joinToChat(long userId, long chatId) {
        try {
            return chatRepository.joinToChat(userId, chatId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
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
