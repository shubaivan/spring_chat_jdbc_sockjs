package com.spdu.web.restcontrollers;

import com.spdu.bll.custom_exceptions.CustomFileException;
import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.interfaces.FileEntityService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.FileEntityDto;
import com.spdu.bll.models.UserDto;
import com.spdu.bll.models.UserRegisterDto;
import com.spdu.bll.services.CustomUserDetailsService;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private final FileEntityService fileEntityService;

    @Autowired
    public UserController(UserService userService, CustomUserDetailsService userDetailsService, FileEntityService fileEntityService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.fileEntityService = fileEntityService;
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity getById(@PathVariable long id) throws SQLException {
        Optional<User> result = userService.getById(id);
        if (result.isPresent()) {
            return new ResponseEntity(result.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity("User not found!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity getAllUsers(Principal principal) {
        List<User> users = userService.getAll(principal.getName());
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @GetMapping("/info")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity getDetails(Principal principal) {
        Optional<User> user = userService.getByEmail(principal.getName());
        if (user.isPresent()) {
            return new ResponseEntity(user, HttpStatus.OK);
        } else {
            return new ResponseEntity("User not found!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity update(@RequestBody UserDto userDTO, Principal principal) {
        Optional<User> user = userService.getByEmail(principal.getName());
        if (user.isPresent()) {
            try {
                UserDto result = userService.update(user.get().getId(), userDTO);
                return new ResponseEntity(result, HttpStatus.OK);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return new ResponseEntity("User not found!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserRegisterDto userRegisterDTO) throws SQLException, UserException {
        Optional<User> result = userService.register(userRegisterDTO);
        if (result.isPresent()) {
            return new ResponseEntity(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity("User doesn't created!", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity uploadAvatar(Principal principal, MultipartFile multipartFile) {
//        try {
//            Optional<User> user = userService.getByEmail(principal.getName());
//            if (user.isPresent()) {
//                Properties properties = new Properties();
//                properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
//
//                String sServerLocation = properties.getProperty("server.upload.docs.path");
//                String path = "\\avatar\\" + principal.getName() + "\\";
//
//                multipartFile.transferTo(fileEntityService.store(multipartFile.getOriginalFilename(), sServerLocation + path));
//
//                FileEntityDto fileEntityDto = new FileEntityDto();
//
//                fileEntityDto.setContentType(multipartFile.getContentType());
//                fileEntityDto.setName(multipartFile.getOriginalFilename());
//                fileEntityDto.setPath(path + multipartFile.getOriginalFilename());
//                fileEntityDto.setOwnerId(user.get().getId());
//
//                FileEntityDto newFile = fileEntityService.save(fileEntityDto);
//                userService.updateAvatar(user.get().getId(), newFile.getId());
//            }
//        } catch (IOException | SQLException | UserException | CustomFileException e) {
//            throw new RuntimeException(e);
//        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
