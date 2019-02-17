package com.spdu.web.viewcontrollers;

import com.spdu.bll.interfaces.MessageService;
import com.spdu.bll.models.ChatMessage;
import com.spdu.bll.models.ChatTyping;
import com.spdu.bll.models.CustomUserDetails;
import com.spdu.dal.repositories.ChatRepositoryImpl;
import com.spdu.domain_models.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.sql.Time;
import java.util.stream.Collectors;

@Controller
public class MessageViewController {

    private final MessageService messageService;

    private final ChatRepositoryImpl chatRepository;

    @Autowired
    public MessageViewController(
            MessageService messageService,
            ChatRepositoryImpl chatRepository
    ) {
        this.messageService = messageService;
        this.chatRepository = chatRepository;
    }

    @MessageMapping("/chat/{id}/sendMessage")
    @SendTo("/topic/public/{id}")
    public ChatMessage sendMessage(
            @Payload ChatMessage message,
            Principal principal
    ) {
        return convertMessage(message, principal);
    }

    @MessageMapping("/chat/{id}/addUser")
    @SendTo("/topic/public/{id}")
    public ChatMessage addUser(
            @Payload ChatMessage message,
            Principal principal,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());

        return convertMessage(message, principal);
    }

    @MessageMapping("/chat/{id}/typing")
    @SendTo("/topic/chat/{id}/typing")
    public ChatTyping typingUser(
            @Payload ChatTyping chatTyping
            ) throws IOException {
        String g = "";

        return chatTyping;
    }

    @MessageMapping("/chat/{id}/leftUser")
    @SendTo("/topic/public/{id}")
    public ChatMessage leftUser(
            @Payload ChatMessage message,
            Principal principal,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        chatRepository.removeChatUser(
                this.getCustomUserDetails(principal).getId(),
                message.getChatId()
        );

        return convertMessage(message, principal);
    }

    private ChatMessage convertMessage(ChatMessage message, Principal principal)
    {
        Integer chatId = message.getChatId();

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();

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

        messageService.send(cud.getUser().getEmail(), messageModel);

        return message;
    }

    private CustomUserDetails getCustomUserDetails(Principal principal)
    {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        return  (CustomUserDetails) token.getPrincipal();
    }
}
