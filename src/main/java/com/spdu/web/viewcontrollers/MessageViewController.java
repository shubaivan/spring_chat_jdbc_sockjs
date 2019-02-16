package com.spdu.web.viewcontrollers;

import com.spdu.bll.interfaces.MessageService;
import com.spdu.bll.models.ChatMessage;
import com.spdu.bll.models.CustomUserDetails;
import com.spdu.domain_models.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.sql.Date;
import java.sql.Time;

@Controller
public class MessageViewController {

    private final MessageService messageService;

    @Autowired
    public MessageViewController(MessageService messageService) {
        this.messageService = messageService;
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
}
