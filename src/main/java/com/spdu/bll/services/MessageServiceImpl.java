package com.spdu.bll.services;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.bll.interfaces.MessageService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.MessageReturnDTO;
import com.spdu.dal.repository.MessageRepository;
import com.spdu.domain_models.entities.Message;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public Optional<MessageReturnDTO> send(String userEmail, Message message) {
        try {
            Optional<User> userOpt = userService.getByEmail(userEmail);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (chatService.userIsPresentInChat(user.getId(), message.getChatId())) {
                    message.setAuthorID(user.getId());
                    long messageId = messageRepository.create(message);

                    Optional<Message> optionalMessage = getById(messageId);
                    if (optionalMessage.isPresent()) {
                        Message createdMessage = optionalMessage.get();

                        MessageReturnDTO messageReturnDTO = new MessageReturnDTO(
                                userEmail, createdMessage.getText(),
                                createdMessage.getDateOfCreated());
                        return Optional.of(messageReturnDTO);
                    }
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Optional.empty();
    }
}