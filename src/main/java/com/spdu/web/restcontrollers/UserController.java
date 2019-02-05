package com.spdu.web.restcontrollers;

import com.spdu.bll.exceptions.UserException;
import com.spdu.bll.interfaces.UserService;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public UserController(UserService userService, CustomUserDetailsService userDetailsService) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
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
