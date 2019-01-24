package com.spdu.web.controllers;

import com.spdu.bll.exceptions.UserException;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.returned_model.UserRegisterDTO;
import com.spdu.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity getById(long id) {
        Optional<User> result = userService.getById(id);
        if (result.isPresent()) {
            return new ResponseEntity(result.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity("User not found!", HttpStatus.BAD_REQUEST);
        }
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
