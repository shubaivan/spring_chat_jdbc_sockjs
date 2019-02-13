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

import javax.servlet.http.HttpServletRequest;
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
    public ModelAndView registerSubmit(HttpServletRequest request, ModelMap modelMap, UserRegisterDto userRegisterDTO) throws UserException, SQLException {
        Optional<User> newUser = userService.register(userRegisterDTO);

        if (newUser.isPresent()) {
            sendToken(getUrl(request), newUser.get());
        }
        modelMap.put("email", userRegisterDTO.getEmail());
        return new ModelAndView("registerSuccess", modelMap);
    }

    @GetMapping("/confirm-account")
    public ModelAndView confirmAccount(String token) {
        try {
            if (userService.confirmAccount(token)) {
                return new ModelAndView("confirmEmail");
            }
        } catch (SQLException | UserException e) {
            throw new RuntimeException(e);
        }
        return new ModelAndView("redirect:/register");
    }

    private void sendToken(String requestUrl, User user) throws SQLException {
        String confirmationToken = userService.setConfirmationToken(user.getUserId());
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + requestUrl + "/confirm-account?token=" + confirmationToken);

        emailSender.sendEmail(mailMessage);
    }

    private String getUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }

        return url.toString();
    }

    @RequestMapping("/mainform")
    public String mainForm() {
        return "mainform";
    }
}
