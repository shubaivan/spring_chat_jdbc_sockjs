package com.spdu.web.restcontrollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.spdu.bll.custom_exceptions.PasswordException;
import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.CustomUserDetails;
import com.spdu.bll.models.UserDto;
import com.spdu.bll.models.UserRegisterDto;
import com.spdu.bll.models.joinChatRequestContentDTO;
import com.spdu.bll.services.ChatServiceImpl;
import com.spdu.bll.services.CustomUserDetailsService;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;
    private ChatServiceImpl chatService;

    @Autowired
    public UserController(
            UserService userService,
            CustomUserDetailsService userDetailsService,
            ChatServiceImpl chatService
    ) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.chatService = chatService;
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
    public ResponseEntity getById(@PathVariable long id) {
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
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();

        try {
            User result = userService.update(cud.getId(), userDTO);
            return new ResponseEntity(new UserDto(result), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/chat")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity postUserToChat(
            Principal principal,
            HttpServletRequest request
    ) throws SQLException, IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        joinChatRequestContentDTO contentMap = this.deserializerToObj(content);

        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
        CustomUserDetails cud = (CustomUserDetails) token.getPrincipal();

        boolean result = getChatService().joinToChat(cud.getUser().getId(), contentMap.getId());
        Map<String, String> map = new HashMap<String, String>();
        if (!result) {
            map.put("status", "user exist in chat");
        } else {
            map.put("status", "ok");
        }

        return new ResponseEntity(map, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserRegisterDto userRegisterDTO) throws SQLException, UserException, PasswordException {
        Optional<User> result = userService.register(userRegisterDTO);
        if (result.isPresent()) {
            return new ResponseEntity(result.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity("User doesn't created!", HttpStatus.BAD_REQUEST);
        }
    }

    private ChatServiceImpl getChatService() {
        return chatService;
    }

    private Map<String, Integer> deserializerToMap(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        @SuppressWarnings("unchecked")
        Map<String, Integer> content = mapper.readValue(json, Map.class);

        return content;
    }

    private joinChatRequestContentDTO deserializerToObj(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(json, joinChatRequestContentDTO.class);
    }
}
