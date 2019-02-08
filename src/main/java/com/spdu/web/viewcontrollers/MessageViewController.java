package com.spdu.web.viewcontrollers;

import com.spdu.bll.interfaces.MessageService;
import com.spdu.bll.models.ChatMessage;
import com.spdu.bll.models.CustomUserDetails;
import com.spdu.domain_models.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
        Integer chatId = message.getChatId();

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();

        Message messageModel = new Message();

        messageModel
                .setChatId(chatId)
                .setText(message.getContent())
                .setMessageType(message.getType());

        messageService.send(cud.getUser().getEmail(), messageModel);

        return message;
    }

    @MessageMapping("/chat/{id}/addUser")
    @SendTo("/topic/public/{id}")
    public ChatMessage addUser(
            @Payload ChatMessage message,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());

        return message;
    }
}
