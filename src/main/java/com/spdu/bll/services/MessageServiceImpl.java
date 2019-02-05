package com.spdu.bll.services;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.bll.interfaces.MessageService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.MessageReturnDto;
import com.spdu.dal.repository.MessageRepository;
import com.spdu.domain_models.entities.Message;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public Optional<Message> getById(long id) {
        return messageRepository.getById(id);
    }

    @Override
    public List<Message> getByChatId(long id) {
        List<Message> messages = messageRepository.getByChatId(id);
        return messages;
    }

    @Override
    public Optional<Message> create(Message message){
        long messageId = messageRepository.create(message);
        return getById(messageId);
    }

    public Optional<MessageReturnDto> send(String userEmail, Message message){
            Optional<User> userOpt = userService.getByEmail(userEmail);
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                if (chatService.userIsPresentInChat(user.getId(), message.getChatId())) {
                    message.setAuthorID(user.getId());
                    long messageId = messageRepository.create(message);
                    Optional<Message> optionalMessage = getById(messageId);

                    if (optionalMessage.isPresent()) {
                        Message createdMessage = optionalMessage.get();
                        MessageReturnDto messageReturnDTO = new MessageReturnDto(
                                userEmail, createdMessage.getText(),
                                createdMessage.getCreatedAt());
                        return Optional.of(messageReturnDTO);
                    }
                }
            }
        return Optional.empty();
    }
}