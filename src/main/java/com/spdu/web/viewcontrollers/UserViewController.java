package com.spdu.web.viewcontrollers;

import com.spdu.bll.custom_exceptions.CustomFileException;
import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.interfaces.FileEntityService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.CustomUserDetails;
import com.spdu.bll.models.FileEntityDto;
import com.spdu.bll.models.UserDto;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Controller
public class UserViewController {
    private final UserService userService;
    private final FileEntityService fileEntityService;

    @Autowired
    public UserViewController(UserService userService, FileEntityService fileEntityService) {
        this.userService = userService;
        this.fileEntityService = fileEntityService;
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

        UserDto result = userService.update(userId, userDTO);
        modelMap.addAttribute("userDTO", result);

        return new ModelAndView("redirect:/profile", modelMap);
    }

    @PutMapping("/profile/avatar")
    public ModelAndView updateUsersAvatar(MultipartFile multipartFile, ModelMap modelMap, Principal principal) {
        try {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
            CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();
            long userId = cud.getId();

            Properties properties = new Properties();
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

            String sServerLocation = properties.getProperty("server.upload.docs.path");
            String path = "/avatar/" + principal.getName() + "/";

            multipartFile.transferTo(fileEntityService.store(multipartFile.getOriginalFilename(), sServerLocation + path));

            FileEntityDto fileEntityDto = new FileEntityDto();

            fileEntityDto.setContentType(multipartFile.getContentType());
            fileEntityDto.setName(multipartFile.getOriginalFilename());
            fileEntityDto.setPath(path + multipartFile.getOriginalFilename());
            fileEntityDto.setOwnerId(userId);

            FileEntityDto newFile = fileEntityService.save(fileEntityDto);
            UserDto updatedUser = userService.updateAvatar(userId, newFile.getId());

            modelMap.addAttribute("userDTO", updatedUser);
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

}
