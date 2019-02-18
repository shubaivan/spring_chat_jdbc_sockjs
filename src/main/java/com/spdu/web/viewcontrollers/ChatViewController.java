package com.spdu.web.viewcontrollers;

import com.spdu.bll.custom_exceptions.ChatException;
import com.spdu.bll.interfaces.ChatService;
import com.spdu.bll.models.ChatDto;
import com.spdu.bll.models.CustomUserDetails;
import com.spdu.domain_models.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("chats")
public class ChatViewController {
    private final ChatService chatServiceImp;

    @Autowired
    public ChatViewController(ChatService chatService) {
        this.chatServiceImp = chatService;
    }

    @RequestMapping("/chat/{id}")
    public String getInfoChat(@PathVariable long id, ModelMap modelMap, Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();

        Optional<Chat> result = chatServiceImp.getById(id);
        Chat chat = result.get();
        String fullName = cud.getUser().getFirstName() + ' ' + cud.getUser().getLastName();

        modelMap.addAttribute("chat", chat);
        modelMap.addAttribute("fullName", fullName);
        modelMap.addAttribute("auth", cud.getUser());

        return "chat";
    }

    @GetMapping
    public String setChatsContent(ModelMap modelMap, Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();
        long userId = cud.getId();

        List<Chat> ownChats = chatServiceImp.getAllOwn(userId);
        List<Chat> allChats = chatServiceImp.getAll(userId);
        List<Chat> allPublic = chatServiceImp.getPublic(userId);

        modelMap.addAttribute("ownChats", ownChats);
        modelMap.addAttribute("allChats", allChats);
        modelMap.addAttribute("allPublic", allPublic);

        modelMap.addAttribute("chatDto", new ChatDto());
        return "mainform";
    }

    @PostMapping("/createchat")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public String createChat(ChatDto chatDto, Principal principal) throws SQLException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();
        chatDto.setOwnerId(cud.getId());
        chatServiceImp.create(chatDto);
        return "redirect:/chats";
    }

    @PutMapping("/chat/update")
    public ModelAndView update(ChatDto chatDto, ModelMap modelMap, Principal principal) throws ChatException, SQLException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();
        long userId = cud.getId();

        ChatDto result = chatServiceImp.update(1, chatDto);
        modelMap.addAttribute("chatDto", result);

        return new ModelAndView("redirect:/chats", modelMap);
    }

    @GetMapping("/chatprofile/{id}")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public String profile(@PathVariable long id, ModelMap modelMap, Principal principal) throws ChatException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();
        if (cud.getId() == chatServiceImp.getById(id).get().getOwnerId()) {
            Optional<Chat> optionalChat = chatServiceImp.getById(id);
            if (optionalChat.isPresent()) {
                modelMap.addAttribute("chatDto", new ChatDto(optionalChat.get()));
            } else {
                throw new ChatException("Chat not found!");
            }
            return "chatprofile";
        } else throw new ChatException("You is not chat owner");
    }

}

