package com.spdu.web.controllers;

import com.spdu.bll.interfaces.MessageService;
import com.spdu.model.entities.Message;
import com.spdu.web.websocket.SocketHandler;
import com.spdu.web.websocket.kop.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("messages")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody Message message) {
        Optional<Message> newMessage = messageService.create(message);
        if (newMessage.isPresent()) {
            return new ResponseEntity(newMessage.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity getById(@PathVariable long id) {
        Optional<Message> newMessage = messageService.getById(id);
        if (newMessage.isPresent()) {
            return new ResponseEntity(newMessage.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/chat/{id}")
    public ResponseEntity getByChatId(@PathVariable long id) {

        List<Message> listMessages = null;
        try {
            listMessages = messageService.getByChatId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!listMessages.isEmpty()) {
            return new ResponseEntity(listMessages, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/send")
    public void sendMessage() {
        System.out.println("PING");
        SocketHandler socketHandler = AppConfig.configSocketHandler;
        socketHandler.sendMess(new TextMessage("PING"));
    }
}
