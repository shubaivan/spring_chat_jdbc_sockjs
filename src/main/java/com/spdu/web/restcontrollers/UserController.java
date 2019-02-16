package com.spdu.web.restcontrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.interfaces.FileEntityService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.UserDto;
import com.spdu.bll.models.UserRegisterDto;
import com.spdu.bll.services.CustomUserDetailsService;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @GetMapping("/chat/{id}")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity getByChatId(@PathVariable long id) throws SQLException, JsonProcessingException {
        List<User> users = userService.getByChatId(id);
        Set<String> props = new HashSet<>();

        props.add("id");
        props.add("firstName");
        props.add("lastName");

        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("listUserSideBarFilter",
                SimpleBeanPropertyFilter.filterOutAllExcept(props));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setFilterProvider(filterProvider);

        return new ResponseEntity(mapper.writeValueAsString(users), HttpStatus.OK);
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
}
