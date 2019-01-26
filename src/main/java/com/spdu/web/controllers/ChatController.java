package com.spdu.web.controllers;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.model.entities.Chat;
import com.spdu.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;

    @Autowired
    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity getById(@PathVariable long id) {
        Optional<Chat> result = chatService.getById(id);
        if (result.isPresent()) {
            return new ResponseEntity(result.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity("Chat not found!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/public")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity getPublicChats() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.getByEmail(email);
        if (user.isPresent()) {
            List<Chat> chats = chatService.getPublic(user.get().getId());
            return new ResponseEntity(chats, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/own")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity getOwnChats() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.getByEmail(email);
        if (user.isPresent()) {
            List<Chat> chats = chatService.getAllOwn(user.get().getId());
            return new ResponseEntity(chats, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity getAllChats() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.getByEmail(email);
        if (user.isPresent()) {
            List<Chat> chats = chatService.getAll(user.get().getId());
            return new ResponseEntity(chats, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity create(@RequestBody Chat chat) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.getByEmail(email);
        if (user.isPresent()) {
            chat.setOwnerId(user.get().getId());
        }
        Optional<Chat> result = chatService.create(chat);
        if (result.isPresent()) {
            return new ResponseEntity(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity("Can't create chat", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("join/{chatId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity joinToChat(@PathVariable long chatId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.getByEmail(email);
        long result = 0;

        if (user.isPresent()) {
            result = chatService.joinToChat(user.get().getId(), chatId);
        }
        if (result != 0) {
            return new ResponseEntity(result, HttpStatus.OK);
        } else {
            return new ResponseEntity("Chat not found!", HttpStatus.BAD_REQUEST);
        }
    }
}
