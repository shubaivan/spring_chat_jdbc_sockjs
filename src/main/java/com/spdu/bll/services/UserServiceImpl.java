package com.spdu.bll.services;

import com.spdu.bll.exceptions.UserException;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.UserRegisterDto;
import com.spdu.dal.repository.ChatRepository;
import com.spdu.dal.repository.UserRepository;
import com.spdu.bll.models.constants.UserRole;
import com.spdu.domain_models.entities.User;
import com.spdu.domain_models.entities.relations.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ChatRepository chatRepository) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    public Optional<User> getById(long id) throws SQLException {
        return userRepository.getById(id);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public Optional<User> register(UserRegisterDto userRegisterDTO) throws UserException, SQLException {
        if (emailExist(userRegisterDTO.getEmail())) {
            throw new UserException("Account with this email is exist!");
        }
        if (!userRegisterDTO.getPassword().equals(userRegisterDTO.getMatchingPassword())) {
            throw new UserException("Password doesn't match!");
        }
        User user = new User();
        String encoded = new BCryptPasswordEncoder().
                encode(userRegisterDTO.getPassword());
        user.setPassword(encoded);
        user.setEmail(userRegisterDTO.getEmail());
        user.setDateOfBirth(userRegisterDTO.getDateOfBirth());
        user.setDateOfRegistration(LocalDateTime.now());
        user.setUserName(userRegisterDTO.getUserName());
        long userId = userRepository.register(user);
        chatRepository.joinToChat(userId, 1);
        setUserRole(UserRole.USER, userId);
        return getById(userId);
    }

    @Override
    public List<User> getAll(String currentUserEmail) {
        return userRepository.getAll()
                .stream()
                .filter(user -> !user.getEmail().equals(currentUserEmail))
                .collect(Collectors.toList());
    }

    private void setUserRole(UserRole role, long userId) throws SQLException {
        UserRoles userRole = new UserRoles();
        userRole.setUserId(userId);
        userRole.setRoleId(role.ordinal() + 1);
        userRepository.setUserRole(userRole);
    }

    private boolean emailExist(String email) {
        Optional<User> user = userRepository.getByEmail(email);
        return user.isPresent();
    }
}
