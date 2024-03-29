package com.spdu.web.viewcontrollers;

import com.spdu.bll.custom_exceptions.CustomFileException;
import com.spdu.bll.custom_exceptions.MessageException;
import com.spdu.bll.interfaces.FileEntityService;
import com.spdu.bll.interfaces.MessageService;
import com.spdu.bll.models.*;
import com.spdu.bll.models.sockets.ChatTyping;
import com.spdu.dal.repositories.ChatRepository;
import com.spdu.domain_models.entities.FileEntity;
import com.spdu.domain_models.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.rmi.AccessException;
import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.Optional;

@Controller
public class MessageViewController {
    private final MessageService messageService;
    private final ChatRepository chatRepository;
    private final FileEntityService fileEntityService;

    @Autowired
    public MessageViewController(
            MessageService messageService,
            ChatRepository chatRepository,
            FileEntityService fileEntityService
    ) {
        this.messageService = messageService;
        this.chatRepository = chatRepository;
        this.fileEntityService = fileEntityService;
    }

    @MessageMapping("/chat/{id}/sendMessage")
    @SendTo("/topic/public/{id}")
    public ChatMessage sendMessage(
            @Payload ChatMessage message,
            Principal principal
    ) throws CustomFileException {
        return convertMessage(message, principal);
    }

    @MessageMapping("/chat/{id}/addUser")
    @SendTo("/topic/public/{id}")
    public ChatMessage addUser(
            @Payload ChatMessage message,
            Principal principal,
            SimpMessageHeaderAccessor headerAccessor
    ) throws CustomFileException {
        headerAccessor.getSessionAttributes().put("username", message.getSender());

        return convertMessage(message, principal);
    }

    @MessageMapping("/chat/{id}/typing")
    @SendTo("/topic/chat/{id}/typing")
    public ChatTyping typingUser(
            @Payload ChatTyping chatTyping
    ) {
        return chatTyping;
    }

    @MessageMapping("/chat/{id}/edit-message")
    @SendTo("/topic/chat/{id}/edit-message")
    public MessageDto editMessage(
            @Payload MessageDto messageDto,
            Principal principal
    ) throws MessageException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();

        try {
            if (cud.getId() != messageDto.getAuthorId()) {
                throw new AccessException("dude, you not owner");
            }
            messageDto.setAuthorId(cud.getId());

            return messageService.updateOptimization(messageDto);
        } catch (Exception e) {
            messageDto.setContent(e.getMessage());
            messageDto.setStatus(0);

            return messageDto;
        }
    }

    @MessageMapping("/chat/{id}/delete-message")
    @SendTo("/topic/chat/{id}/delete-message")
    public MessageDto deleteMessage(
            @Payload MessageDto messageDto,
            Principal principal
    ) {

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();

        try {
            if (cud.getId() != messageDto.getAuthorId()) {
                throw new AccessException("dude, you not owner");
            }
            messageDto.setAuthorId(cud.getId());
            messageService.removeMessage(messageDto.getId(), messageDto.getAuthorId());
            messageDto.setStatus(1);

            return messageDto;
        } catch (Exception e) {
            messageDto.setContent(e.getMessage());
            messageDto.setStatus(0);

            return messageDto;
        }
    }

    @MessageMapping("/chat/{id}/leftUser")
    @SendTo("/topic/public/{id}")
    public ChatMessage leftUser(
            @Payload ChatMessage message,
            Principal principal,
            SimpMessageHeaderAccessor headerAccessor
    ) throws CustomFileException {
        chatRepository.removeChatUser(
                this.getCustomUserDetails(principal).getId(),
                message.getChatId()
        );

        return convertMessage(message, principal);
    }

    private ChatMessage convertMessage(ChatMessage message, Principal principal) throws CustomFileException {
        Integer chatId = message.getChatId();

        CustomUserDetails cud = this.getCustomUserDetails(principal);

        Date date = new Date(System.currentTimeMillis());
        message.setCreatedDate(date);

        Time time = new Time(System.currentTimeMillis());
        message.setCreatedTime(time);

        Message messageModel = new Message();

        messageModel
                .setChatId(chatId)
                .setText(message.getContent())
                .setMessageType(message.getType())
                .setCreatedTime(message.getCreatedTime())
                .setCreatedDate(message.getCreatedDate());

        Optional<MessageReturnDto> optionalMessage = messageService.send(cud.getUser().getEmail(), messageModel);
        Optional<FileEntity> fileOptional = Optional.ofNullable(fileEntityService.getFileEntity(cud.getUser().getId()));

        if (optionalMessage.isPresent()) {
            MessageReturnDto messageReturnDto = optionalMessage.get();
            message.setId(messageReturnDto.getId());
        }

        if (fileOptional.isPresent()) {
            FileEntityDto fileEntityDto = new FileEntityDto(fileOptional.get());
            message.setAvatarId(fileEntityDto.getId());
        }

        return message;
    }

    private CustomUserDetails getCustomUserDetails(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        return (CustomUserDetails) token.getPrincipal();
    }
}
