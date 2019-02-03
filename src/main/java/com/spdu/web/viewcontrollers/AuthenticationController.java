package com.spdu.web.viewcontrollers;

import com.spdu.bll.exceptions.UserException;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.UserRegisterDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.SQLException;

@Controller
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ModelAndView index(Model model, Principal principal) {
        model.addAttribute("message", "You are logged in as " + principal.getName());
//        return "index";

        return new ModelAndView("redirect:" + "chats");
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(ModelMap modelMap) {
        modelMap.addAttribute("userRegisterDTO", new UserRegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(UserRegisterDto userRegisterDTO) throws UserException, SQLException {
        userService.register(userRegisterDTO);
        return "login";
    }

    @RequestMapping("/mainform")
    public String mainForm() {
        return "mainform";
    }
}
