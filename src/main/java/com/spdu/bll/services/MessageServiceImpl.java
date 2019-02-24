package com.spdu.bll.services;

import com.spdu.bll.custom_exceptions.MessageException;
import com.spdu.bll.interfaces.ChatService;
import com.spdu.bll.interfaces.MessageService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.MessageDto;
import com.spdu.bll.models.MessageReturnDto;
import com.spdu.bll.models.MessagesRequestContentDto;
import com.spdu.dal.repositories.MessageRepository;
import com.spdu.domain_models.entities.Message;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository,
                              UserService userService, ChatService chatService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.chatService = chatService;
    }

    @Override
    public MessageDto update(long id, MessageDto messageDto) throws SQLException, MessageException {
        Optional<Message> messageOptional = getById(id);
        if (messageOptional.isPresent()) {
            Message oldMessage = messageOptional.get();
            oldMessage.setText(messageDto.getContent());
            Message modifiedMessage = messageRepository.update(id, oldMessage);
            return new MessageDto(modifiedMessage);
        } else {
            throw new MessageException("Chat not found");
        }
    }

    @Override
    public Optional<Message> getById(long id) throws EmptyResultDataAccessException {
        return messageRepository.getById(id);
    }

    @Override
    public List<Message> getMessages(MessagesRequestContentDto requestContentDTO) throws EmptyResultDataAccessException {
        return messageRepository.getMessages(
                requestContentDTO.getId(),
                Optional.ofNullable(requestContentDTO.getKeyword()));
    }

    @Override
    public Optional<Message> create(Message message) {
        try {
            long messageId = messageRepository.create(message);
            return getById(messageId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<MessageReturnDto> send(String userEmail, Message message) {
        Optional<User> userOpt = userService.getByEmail(userEmail);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (chatService.userIsPresentInChat(user.getId(), message.getChatId())) {
                message.setAuthorID(user.getId());

                Optional<Message> optionalMessage;
                try {
                    long messageId = messageRepository.create(message);
                    optionalMessage = getById(messageId);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (optionalMessage.isPresent()) {
                    Message createdMessage = optionalMessage.get();
                    MessageReturnDto messageReturnDTO = new MessageReturnDto(
                            userEmail, createdMessage.getText(),
                            createdMessage.getCreatedAt(), createdMessage.getId());
                    return Optional.of(messageReturnDTO);
                }
            }
        }
        return Optional.empty();
    }
}