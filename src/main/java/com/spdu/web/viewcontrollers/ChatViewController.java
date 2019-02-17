package com.spdu.web.viewcontrollers;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.ChatDto;
import com.spdu.bll.models.CustomUserDetails;
import com.spdu.bll.services.ChatServiceImpl;
import com.spdu.domain_models.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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

        return "mainform";
    }

    @GetMapping("/new")
    public String createForm(ModelMap modelMap) {
        modelMap.addAttribute("chatDto", new ChatDto());
        return "/createchat";
    }

    @PostMapping("/createchat")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public String createChat(ChatDto chatDto, Principal principal) throws SQLException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();
        chatDto.setOwnerId(cud.getId());
        chatServiceImp.create(chatDto);
        return "redirect:/mainform";
    }

}

