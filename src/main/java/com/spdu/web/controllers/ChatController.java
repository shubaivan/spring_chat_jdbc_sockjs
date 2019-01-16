package com.spdu.web.controllers;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.model.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping()
    public ResponseEntity<Chat> create(@RequestBody Chat chat) {
       return new ResponseEntity<>(chatService.create(chat), HttpStatus.OK);
    }
}
