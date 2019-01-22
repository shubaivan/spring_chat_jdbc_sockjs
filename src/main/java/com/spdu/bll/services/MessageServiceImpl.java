package com.spdu.bll.services;

import com.spdu.bll.interfaces.MessageService;
import com.spdu.dal.repository.MessageRepository;
import com.spdu.model.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Optional<Message> getById(long id) {
        try {
            return messageRepository.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Message> create(Message message) {
        try {
            long messageId = messageRepository.create(message);
            return getById(messageId);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return Optional.empty();
        }
    }
}