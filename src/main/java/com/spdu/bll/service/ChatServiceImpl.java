package com.spdu.bll.service;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.dal.repository.ChatRepository;
import com.spdu.model.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("chatService")
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository){
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat create(Chat chat) {
        chat.setDateOfCreated(LocalDateTime.now());
        long chatId = chatRepository.create(chat);
        return chatRepository.findById(Chat.class, chatId).get();
    }
}
