package com.spdu.bll.service;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.dal.repository.ChatRepository;
import com.spdu.model.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("chatService")
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Override
    public Chat create(Chat chat) {
        long chatId = chatRepository.create(chat);
        return chatRepository.findById(Chat.class, chatId).get();
    }
}
