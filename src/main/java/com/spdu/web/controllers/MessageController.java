package com.spdu.web.controllers;

import com.spdu.bll.interfaces.MessageService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.services.CustomUserDetailsService;
import com.spdu.model.entities.Message;
import com.spdu.model.entities.User;
import com.spdu.web.websocket.SocketHandler;
import com.spdu.web.websocket.kop.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.socket.TextMessage;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/messages")
public class MessageController {
    private MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity save(@RequestBody Message message) {
        Optional<Message> newMessage = messageService.create(message);
        if (newMessage.isPresent()) {
            return new ResponseEntity(newMessage.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getById(long id) {
        Optional<Message> newMessage = messageService.getById(id);
        if (newMessage.isPresent()) {
            return new ResponseEntity(newMessage.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getByChatId(long id) throws SQLException {
        List<Message> listMessages = messageService.getByChatId(id);
        if (!listMessages.isEmpty()) {
            return new ResponseEntity(listMessages, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public void sendMessage() {
        System.out.println("PING");
        SocketHandler socketHandler = AppConfig.configSocketHandler;
        socketHandler.sendMess(new TextMessage("PING"));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/sout")
    public void soutMessage() {
        System.out.println("PING");
    }
}
