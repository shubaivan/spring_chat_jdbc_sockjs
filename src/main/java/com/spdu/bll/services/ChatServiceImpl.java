package com.spdu.bll.services;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.dal.repository.ChatRepository;
import com.spdu.domain_models.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public Optional<Chat> getById(long id) throws EmptyResultDataAccessException {
        return chatRepository.getById(id);
    }

    @Override
    public Optional<Chat> create(Chat chat) throws SQLException {
        if (chat == null) {
            throw new RuntimeException("Chat is empty!");
        }

        long chatId = chatRepository.create(chat);
        joinToChat(chat.getOwnerId(), chatId);
        return getById(chatId);
    }

    @Override
    public boolean joinToChat(long userId, long chatId) {
        if (userIsPresentInChat(userId, chatId)) {
            return false;
        }
        long result = chatRepository.joinToChat(userId, chatId);

        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean userIsPresentInChat(long userId, long chatId) throws EmptyResultDataAccessException {
        return chatRepository.userIsPresentInChat(userId, chatId);
    }

    @Override
    public List<Chat> getAll(long userId) throws EmptyResultDataAccessException {
        return chatRepository.getAll(userId);
    }

    @Override
    public List<Chat> getAllOwn(long userId) throws EmptyResultDataAccessException {
        return chatRepository.getAllOwn(userId);
    }

    @Override
    public List<Chat> getPublic(long userId) throws EmptyResultDataAccessException {
        return chatRepository.getPublic(userId);
    }
}
