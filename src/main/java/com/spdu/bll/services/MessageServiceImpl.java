package com.spdu.bll.services;

import com.spdu.bll.interfaces.MessageService;
import com.spdu.dal.repository.MessageRepository;
import com.spdu.model.entities.Message;
import com.spdu.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Message> getByChatId(long id) throws SQLException {
        List<Message> messages = messageRepository.getAllMessages()
                .stream()
                .filter(message -> message.getChatId() == id)
                .collect(Collectors.toList());
        return messages;
    }

    @Override
    public List<Message> getAllMessages() throws SQLException {
        return messageRepository.getAllMessages();
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