package com.spdu.web.viewcontrollers;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.domain_models.entities.Chat;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("chats")
public class ChatViewController {
    private final ChatService chatService;
    private final UserService userService;

    @Autowired
    public ChatViewController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @GetMapping
    public String registerForm(ModelMap modelMap) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userService.getByEmail(email);
        if (user.isPresent()) {
            List<Chat> chats = chatService.getAllOwn(user.get().getId());
            modelMap.addAttribute("chats", chats);
        }
        return "chats";
    }
}
