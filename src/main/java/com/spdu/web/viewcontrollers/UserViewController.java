package com.spdu.web.viewcontrollers;

import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.UserDto;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;

@Controller
public class UserViewController {
    private final UserService userService;

    @Autowired
    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(ModelMap modelMap, Principal principal) {
        Optional<User> user = userService.getByEmail(principal.getName());
        if (user.isPresent()) {
            modelMap.addAttribute("userDTO", new UserDto(user.get()));
        }
        return "profile";
    }

    @PutMapping("/profile/update")
    public String updateUserInfoSubmit(UserDto userDTO, ModelMap modelMap, Principal principal) throws UserException, SQLException {
        Optional<User> user = userService.getByEmail(principal.getName());
        if (user.isPresent()) {
            UserDto result = userService.update(user.get().getId(), userDTO);
            modelMap.addAttribute("userDTO", result);
        }
        return "profile";
    }
}
