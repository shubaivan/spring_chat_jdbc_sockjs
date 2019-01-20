package com.spdu.bll.services;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.dal.repository.ChatRepository;
import com.spdu.model.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat getById(long id) {
        try {
            return chatRepository.getById(id);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
