package com.spdu.web.viewcontrollers;

import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.UserRegisterDto;
import com.spdu.domain_models.entities.User;
import com.spdu.web.helpers.EmailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;

@Controller
public class AuthenticationController {
    private final UserService userService;
    private final EmailSender emailSender;

    public AuthenticationController(UserService userService, EmailSender emailSender) {
        this.userService = userService;
        this.emailSender = emailSender;
    }

    @GetMapping("/")
    public ModelAndView index(Model model, Principal principal) {
        model.addAttribute("message", "You are logged in as " + principal.getName());
        return new ModelAndView("redirect:" + "chats");
    }

    @GetMapping("/loginAction")
    public ModelAndView loginAction() {
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
        Optional<User> newUser = userService.register(userRegisterDTO);

        if (newUser.isPresent()) {
            sendToken(newUser.get());
        }
        return "login";
    }

    private void sendToken(User user) throws SQLException {
        String confirmationToken = userService.setConfirmationToken(user.getUserId());
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + "http://localhost:8080/confirm-account?token=" + confirmationToken);

        emailSender.sendEmail(mailMessage);
    }

    @RequestMapping("/mainform")
    public String mainForm() {
        return "mainform";
    }
}
