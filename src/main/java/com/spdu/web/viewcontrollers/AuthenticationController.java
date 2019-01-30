package com.spdu.web.viewcontrollers;

import com.spdu.bll.exceptions.UserException;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.UserRegisterDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(ModelMap modelMap) {
        modelMap.addAttribute("userRegisterDTO", new UserRegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(UserRegisterDTO userRegisterDTO) {
        try {
            userService.register(userRegisterDTO);
        } catch (UserException e) {
            e.printStackTrace();
        }
        return "login";
    }
}
