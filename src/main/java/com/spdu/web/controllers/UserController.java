package com.spdu.web.controllers;

import com.spdu.bll.exceptions.UserException;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.returned_model.UserRegisterDTO;
import com.spdu.bll.services.CustomUserDetailsService;
import com.spdu.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public UserController(UserService userService, CustomUserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping({"id"})
    public ResponseEntity getById(@PathVariable long id) {
        Optional<User> result = userService.getById(id);
        if (result.isPresent()) {
            return new ResponseEntity(result.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity("User not found!", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity getAllUsers() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<User> users = userService.getAll(email);
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(value = "/info")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity getDetails() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ResponseEntity(email, HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(value = "/register")
    public ResponseEntity register(@RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            Optional<User> result = userService.register(userRegisterDTO);
            if (result.isPresent()) {
                return new ResponseEntity(result.get(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity("User doesn't created!", HttpStatus.BAD_REQUEST);
            }
        } catch (UserException e) {
            e.printStackTrace();
            return new ResponseEntity(e, HttpStatus.BAD_REQUEST);
        }
    }
}
