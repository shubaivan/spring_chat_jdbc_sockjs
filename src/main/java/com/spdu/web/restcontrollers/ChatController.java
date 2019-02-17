package com.spdu.web.restcontrollers;

import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.interfaces.ChatService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.ChatDto;
import com.spdu.bll.models.CustomUserDetails;
import com.spdu.bll.models.UserRegisterDto;
import com.spdu.domain_models.entities.Chat;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity getById(@PathVariable long id) {
        Optional<Chat> result = chatService.getById(id);
        if (result.isPresent()) {
            return new ResponseEntity(result.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity("Chat not found!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/public")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity getPublicChats(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();

        List<Chat> chats = chatService.getPublic(cud.getId());
        return new ResponseEntity(chats, HttpStatus.OK);
    }

    @GetMapping("/own")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity getOwnChats(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();

        List<Chat> chats = chatService.getAllOwn(cud.getId());
        return new ResponseEntity(chats, HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity getAllChats(Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();

        List<Chat> chats = chatService.getAll(cud.getId());
        return new ResponseEntity(chats, HttpStatus.OK);
    }

    @PostMapping("/createchat")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity create(@RequestBody ChatDto chatDto, Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();
        chatDto.setOwnerId(cud.getId());
        Optional<Chat> result;
        try {
            result = chatService.create(chatDto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (result.isPresent()) {
            return new ResponseEntity(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity("Can't create chat", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("join/{chatId}")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity joinToChat(@PathVariable long chatId, Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();

        boolean result = chatService.joinToChat(cud.getId(), chatId);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}
