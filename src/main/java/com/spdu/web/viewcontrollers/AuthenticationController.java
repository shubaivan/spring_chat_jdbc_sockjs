package com.spdu.web.viewcontrollers;

import com.spdu.bll.custom_exceptions.PasswordException;
import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.ResetPasswordDto;
import com.spdu.bll.models.UserRegisterDto;
import com.spdu.bll.models.validator.DtoValidator;
import com.spdu.domain_models.entities.User;
import com.spdu.web.helpers.EmailSender;
import com.spdu.web.helpers.URLHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;

@Controller
public class AuthenticationController {
    private final DtoValidator dtoValidator;
    private final UserService userService;
    private final EmailSender emailSender;
    private final URLHelper urlHelper;

    public AuthenticationController(DtoValidator dtoValidator, UserService userService,
                                    EmailSender emailSender, URLHelper urlHelper) {
        this.dtoValidator = dtoValidator;
        this.userService = userService;
        this.emailSender = emailSender;
        this.urlHelper = urlHelper;
    }

    @GetMapping("/")
    public ModelAndView index(Model model, Principal principal) {
        model.addAttribute("message", "You are logged in as " + principal.getName());
        return new ModelAndView("redirect:/chats");
    }

    @GetMapping("/loginAction")
    public ModelAndView loginAction() {
        return new ModelAndView("redirect:/chats");
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(ModelMap modelMap) throws IllegalArgumentException {
        modelMap.addAttribute("userRegisterDTO", new UserRegisterDto());
        return "register";
    }

    @PostMapping("/register")
    public ModelAndView registerSubmit(HttpServletRequest request, ModelMap modelMap,
                                       UserRegisterDto userRegisterDTO) throws UserException, SQLException,
            PasswordException, IllegalArgumentException {
        try {
            dtoValidator.validateUserRegisterDto(userRegisterDTO);
            Optional<User> newUser = userService.register(userRegisterDTO);
            if (newUser.isPresent()) {
                sendToken(urlHelper.getUrl(request), newUser.get());
            }
            modelMap.put("email", userRegisterDTO.getEmail());
            return new ModelAndView("registerSuccess", modelMap);
        } catch (IllegalArgumentException e) {
            return new ModelAndView("registerrules");
        }
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

        String titleMessage = "Complete Registration!";
        String bodyMessage = "To confirm your account, please click here : "
                + requestUrl + "/confirm-account?token=" + confirmationToken;

        emailSender.sendEmail(user.getEmail(), titleMessage, bodyMessage);
    }

    @RequestMapping("/mainform")
    public String mainForm() {
        return "mainform";
    }

    @GetMapping("/reset-password")
    public String resetPasswordForm() {
        return "resetPassword";
    }

    @PostMapping("/reset-password")
    public ModelAndView resetPassword(HttpServletRequest request, String email, ModelMap modelMap) throws SQLException {
        Optional<User> optionalUser = userService.getByEmail(email);

        if (optionalUser.isPresent()) {
            String confirmationToken = userService.setConfirmationToken(optionalUser.get().getUserId());

            String titleMessage = "Reset password!";
            String bodyMessage = "To reset your password, please click here : "
                    + urlHelper.getUrl(request) + "/check-token?email=" + email + "&token=" + confirmationToken;

            emailSender.sendEmail(email, titleMessage, bodyMessage);

            modelMap.put("email", email);
            return new ModelAndView("rsPasswordLinkSent", modelMap);
        }
        return new ModelAndView("redirect:/register");
    }

    @GetMapping("/check-token")
    public ModelAndView resetPassword(ModelMap modelMap, String email, String token) {
        try {
            if (userService.checkTokenForResetPassword(email, token)) {
                ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
                resetPasswordDto.setEmail(email);
                modelMap.put("resetPasswordDto", resetPasswordDto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new ModelAndView("newPassword", modelMap);
    }

    @PutMapping("/new-password")
    public ModelAndView newPassword(ResetPasswordDto passwordDto) {
        try {
            dtoValidator.validateResetPasswordDto(passwordDto);
        } catch (IllegalArgumentException e) {
            return new ModelAndView("newpasswordrules");
        }
        try {
            userService.resetPassword(passwordDto);
        } catch (UserException | PasswordException e) {
            throw new RuntimeException(e);
        }
        return new ModelAndView("redirect:/login");
    }
}
