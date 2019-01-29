package com.spdu.web.viewcontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserViewController {

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
