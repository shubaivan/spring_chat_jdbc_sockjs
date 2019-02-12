package com.spdu.web.viewcontrollers;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.ChatDto;
import com.spdu.bll.models.CustomUserDetails;
import com.spdu.domain_models.entities.Chat;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("chats")
public class ChatViewController {
    private final ChatService chatService;
    private final UserService userService;

    @Autowired
    public ChatViewController(
            ChatService chatService,
            UserService userService
    ) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @RequestMapping("/chat/{id}")
    public String getInfoChat(@PathVariable long id, ModelMap modelMap, Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();

        Optional<Chat> result = chatService.getById(id);
        Chat chat = result.get();
        String fullName = cud.getUser().getFirstName() + ' ' + cud.getUser().getLastName();

        modelMap.addAttribute("chat", chat);
        modelMap.addAttribute("fullName", fullName);

        return "chat";
    }

    @GetMapping
    public String setChatsContent(ModelMap modelMap, Principal principal) {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();
        long userId = cud.getId();

        List<Chat> ownChats = chatService.getAllOwn(userId);
        List<Chat> allChats = chatService.getAll(userId);
        List<Chat> allPublic = chatService.getPublic(userId);

        modelMap.addAttribute("ownChats", ownChats);
        modelMap.addAttribute("allChats", allChats);
        modelMap.addAttribute("allPublic", allPublic);

        return "mainform";
    }
}

