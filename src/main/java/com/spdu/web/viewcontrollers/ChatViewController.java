package com.spdu.web.viewcontrollers;

import com.spdu.bll.interfaces.ChatService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.dal.repository.UserRepository;
import com.spdu.domain_models.entities.Chat;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("chats")
public class ChatViewController {
    private final ChatService chatService;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public ChatViewController(
            ChatService chatService,
            UserService userService,
            UserRepository userRepository
    ) {
        this.chatService = chatService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @RequestMapping("/area")
    public String login() {
        return "messagearea";
    }

    @GetMapping
    public String setChatsContent(ModelMap modelMap, Principal principal) {
        Optional<User> user = userRepository.getByUserName(principal.getName());
        if (user.isPresent()) {
            List<Chat> ownChats = chatService.getAllOwn(user.get().getId());
            List<Chat> allChats = chatService.getAll(user.get().getId());
            List<Chat> allPublic = chatService.getPublic(user.get().getId());
            modelMap.addAttribute("ownChats", ownChats);
            modelMap.addAttribute("allChats", allChats);
            modelMap.addAttribute("allPublic", allPublic);
        }
        return "mainform";
    }
}
