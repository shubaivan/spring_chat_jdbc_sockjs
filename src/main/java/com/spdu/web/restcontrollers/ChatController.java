package com.spdu.web.restcontrollers;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.domain_models.entities.Chat;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/chats")
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
        try {
            Optional<Chat> result = chatService.getById(id);
            if (result.isPresent()) {
                return new ResponseEntity(result.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity("Chat not found!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception excetion) {
            return new ResponseEntity("Chat not found! " + excetion.getStackTrace(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/public")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity getPublicChats() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = userService.getByEmail(email);
            if (user.isPresent()) {
                List<Chat> chats = chatService.getPublic(user.get().getId());
                return new ResponseEntity(chats, HttpStatus.OK);
            } else {
                return new ResponseEntity("User not found!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            return new ResponseEntity(exception.getStackTrace(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/own")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity getOwnChats() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = userService.getByEmail(email);
            if (user.isPresent()) {
                List<Chat> chats = chatService.getAllOwn(user.get().getId());
                return new ResponseEntity(chats, HttpStatus.OK);
            } else {
                return new ResponseEntity("User not found!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            return new ResponseEntity(exception.getStackTrace(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity getAllChats() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = userService.getByEmail(email);
            if (user.isPresent()) {
                List<Chat> chats = chatService.getAll(user.get().getId());
                return new ResponseEntity(chats, HttpStatus.OK);
            } else {
                return new ResponseEntity("User not found!", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            return new ResponseEntity(exception.getStackTrace(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity create(@RequestBody Chat chat) {
        try {
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
        } catch (Exception exception) {
            return new ResponseEntity(exception.getStackTrace(), HttpStatus.BAD_REQUEST);
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
