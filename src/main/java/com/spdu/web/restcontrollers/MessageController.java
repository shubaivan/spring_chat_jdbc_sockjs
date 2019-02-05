package com.spdu.web.restcontrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spdu.bll.interfaces.MessageService;
import com.spdu.bll.models.MessageReturnDto;
import com.spdu.domain_models.entities.Message;
import com.spdu.web.websocket.SocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/messages")
public class MessageController {
    private final MessageService messageService;
    private final SocketHandler socketHandler;

    @Autowired
    public MessageController(MessageService messageService, SocketHandler socketHandler) {
        this.messageService = messageService;
        this.socketHandler = socketHandler;
    }

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity create(@RequestBody Message message) {
        Optional<Message> newMessage = messageService.create(message);
        if (newMessage.isPresent()) {
            return new ResponseEntity(newMessage.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity getById(@PathVariable long id) {
        Optional<Message> newMessage = messageService.getById(id);
        if (newMessage.isPresent()) {
            return new ResponseEntity(newMessage.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/chat/{id}")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity getByChatId(@PathVariable long id) {
        List<Message> listMessages = messageService.getByChatId(id);
        return new ResponseEntity(listMessages, HttpStatus.OK);
    }

    @PostMapping("/default-chat")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity sendMessage(@RequestBody Message message, Principal principal) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Optional<MessageReturnDto> newMessage = messageService.send(principal.getName(), message);
            if (newMessage.isPresent()) {
                socketHandler.sendMess(new TextMessage(mapper.writeValueAsString(newMessage.get())));
                return new ResponseEntity(newMessage.get(), HttpStatus.CREATED);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
