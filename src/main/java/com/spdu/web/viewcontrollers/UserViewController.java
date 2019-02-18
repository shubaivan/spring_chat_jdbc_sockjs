package com.spdu.web.viewcontrollers;

import com.spdu.bll.custom_exceptions.CustomFileException;
import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.CustomUserDetails;
import com.spdu.bll.models.FileEntityDto;
import com.spdu.bll.models.UserDto;
import com.spdu.domain_models.entities.User;
import com.spdu.web.helpers.EmailSender;
import com.spdu.web.helpers.FileUploader;
import com.spdu.web.helpers.URLHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class UserViewController {
    private final UserService userService;
    private final FileUploader fileUploader;
    private final URLHelper urlHelper;
    private final EmailSender emailSender;

    @Autowired
    public UserViewController(UserService userService, FileUploader fileUploader,
                              URLHelper urlHelper, EmailSender emailSender) {
        this.userService = userService;
        this.fileUploader = fileUploader;
        this.urlHelper = urlHelper;
        this.emailSender = emailSender;
    }

    @GetMapping("/profile")
    public String profile(ModelMap modelMap, Principal principal) {
        Optional<User> optionalUser = userService.getByEmail(principal.getName());
        if (optionalUser.isPresent()) {
            modelMap.addAttribute("userDTO", new UserDto(optionalUser.get()));
        } else {
            throw new UsernameNotFoundException(principal.getName() + " - user not found!");
        }

        return "profile";
    }

    @PutMapping("/profile/update")
    public ModelAndView updateUserInfoSubmit(UserDto userDTO, ModelMap modelMap, Principal principal) throws UserException, SQLException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();
        long userId = cud.getId();

        User result = userService.update(userId, userDTO);

        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(token.getAuthorities());
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(result, userService.getUserRole(result.getId())),
                token.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        modelMap.addAttribute("userDTO", new UserDto(result));

        return new ModelAndView("redirect:/profile", modelMap);
    }

    @PutMapping("/profile/avatar")
    public ModelAndView updateUsersAvatar(MultipartFile multipartFile, ModelMap modelMap, Principal principal) {
        try {
            if (!multipartFile.isEmpty()) {
                UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
                CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();
                long userId = cud.getId();
                String path = "/avatar/" + principal.getName() + "/";

                FileEntityDto newFile = fileUploader.uploadFile(multipartFile, path, userId);
                UserDto updatedUser = userService.updateAvatar(userId, newFile.getId());

                modelMap.addAttribute("userDTO", updatedUser);
            }
        } catch (IOException | SQLException | UserException | CustomFileException e) {
            throw new RuntimeException(e);
        }

        return new ModelAndView("redirect:/profile", modelMap);
    }

    @GetMapping("/users")
    public String getAllUsers(ModelMap modelMap, Principal principal) {
        List<User> users = userService.getAll(principal.getName());
        modelMap.addAttribute("users", users);

        return "users";
    }

    @GetMapping("/users/{id}")
    public String getById(ModelMap modelMap, @PathVariable String id) {
        UserDto user = new UserDto(userService.getById(Long.valueOf(id)).get());
        modelMap.addAttribute("user", user);
        return "userinfo";
    }

    @PostMapping("/users/invite")
    public String sendInvite(ModelMap modelMap, Principal principal, String email, HttpServletRequest request) {
        List<User> users = userService.getAll(principal.getName());
        modelMap.addAttribute("users", users);

        if (!userService.emailExist(email)) {
            String titleMessage = "Complete your registration!";
            String bodyMessage = "Your friend " + principal.getName()
                    + "sent an invitation to SPD-Talks!\n"
                    + "Click a link to complete your registration!\n"
                    + urlHelper.getUrl(request) + "/register";
            emailSender.sendEmail(email, titleMessage, bodyMessage);
        }
        return "users";
    }
}
