package com.spdu.web.viewcontrollers;

import com.spdu.bll.custom_exceptions.CustomFileException;
import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.interfaces.FileEntityService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.FileEntityDto;
import com.spdu.bll.models.UserDto;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
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

    @PutMapping("/profile/avatar")
    public String updateUsersAvatar(MultipartFile multipartFile, ModelMap modelMap, Principal principal) {
        try {
            Optional<User> user = userService.getByEmail(principal.getName());
            if (user.isPresent()) {
                Properties properties = new Properties();
                properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

                String sServerLocation = properties.getProperty("server.upload.docs.path");
                String path = "\\avatar\\" + principal.getName() + "\\";

                multipartFile.transferTo(fileEntityService.store(multipartFile.getOriginalFilename(), sServerLocation + path));

                FileEntityDto fileEntityDto = new FileEntityDto();

                fileEntityDto.setContentType(multipartFile.getContentType());
                fileEntityDto.setName(multipartFile.getOriginalFilename());
                fileEntityDto.setPath(path + multipartFile.getOriginalFilename());
                fileEntityDto.setOwnerId(user.get().getId());

                FileEntityDto newFile = fileEntityService.save(fileEntityDto);
                userService.updateAvatar(user.get().getId(), newFile.getId());

                modelMap.addAttribute("userDTO", new UserDto(user.get()));
            }
        } catch (IOException | SQLException | UserException | CustomFileException e) {
            throw new RuntimeException(e);
        }
        return "profile";
    }
}
