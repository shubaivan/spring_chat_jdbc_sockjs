package com.spdu.web.controllers;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.model.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;

@Controller
@RequestMapping("chats")
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity getById(long id) {
        Optional<Chat> result = chatService.getById(id);
        if (result.isPresent()) {
            return new ResponseEntity(result.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity("Chat not found!", HttpStatus.BAD_REQUEST);
        }
    }
}
