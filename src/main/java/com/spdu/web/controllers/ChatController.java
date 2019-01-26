package com.spdu.web.controllers;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.model.entities.Chat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ChatController {
    private final ChatService chatService;
    private static final Logger logger =
            LoggerFactory.getLogger(ChatController.class);

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "chats")
    public ResponseEntity getById(long id) {
        Optional<Chat> result = chatService.getById(id);
        if (result.isPresent()) {
            return new ResponseEntity(result.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity("Chat not found!", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "chats")
    public ResponseEntity create(@RequestBody Chat chat) {
        Optional<Chat> result = chatService.create(chat);
        if (result.isPresent()) {
            return new ResponseEntity(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity("Can't create chat", HttpStatus.BAD_REQUEST);
        }
    }
}
