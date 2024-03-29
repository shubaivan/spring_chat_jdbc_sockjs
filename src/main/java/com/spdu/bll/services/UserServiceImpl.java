package com.spdu.bll.services;

import com.spdu.bll.custom_exceptions.PasswordException;
import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.ResetPasswordDto;
import com.spdu.bll.models.UserDto;
import com.spdu.bll.models.UserRegisterDto;
import com.spdu.dal.repositories.ChatRepository;
import com.spdu.dal.repositories.ConfirmationTokenRepository;
import com.spdu.dal.repositories.UserRepository;
import com.spdu.bll.models.constants.UserRole;
import com.spdu.domain_models.entities.ConfirmationToken;
import com.spdu.domain_models.entities.User;
import com.spdu.domain_models.entities.relations.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ChatRepository chatRepository,
                           ConfirmationTokenRepository confirmationTokenRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Optional<User> getById(long id) throws EmptyResultDataAccessException {
        return userRepository.getById(id);
    }

    @Override
    public List<User> getByChatId(long id) throws EmptyResultDataAccessException {
        return userRepository.getByChatId(id);
    }

    @Override
    public Optional<User> getByEmail(String email) throws EmptyResultDataAccessException {
        return userRepository.getByEmail(email);
    }

    @Override
    public Optional<User> getByUserName(String name) throws EmptyResultDataAccessException {
        return userRepository.getByUserName(name);
    }

    @Override
    public User update(long id, UserDto user) throws SQLException, UserException {
        Optional<User> optionalUser = getById(id);
        if (optionalUser.isPresent()) {
            User oldUser = optionalUser.get();

            oldUser.setFirstName(user.getFirstName());
            oldUser.setLastName(user.getLastName());
            oldUser.setDateOfBirth(user.getDateOfBirth());
            oldUser.setUserName(user.getUserName());
            oldUser.setUrlFacebook(user.getUrlFacebook());
            oldUser.setUrlGit(user.getUrlGit());
            oldUser.setUrlLinkedin(user.getUrlLinkedin());

            User modifiedUser = userRepository.update(id, oldUser);
            return modifiedUser;
        } else {
            throw new UserException("User not found");
        }
    }

    @Override
    public UserDto updateAvatar(long id, long fileId) throws SQLException, UserException {
        return new UserDto(userRepository.updateAvatar(id, fileId));
    }

    @Override
    public Optional<User> register(UserRegisterDto userRegisterDto) throws UserException,
            PasswordException, SQLException {
        if (emailExist(userRegisterDto.getEmail())) {
            throw new UserException("Account with this email is exist!");
        }

        if (!userRegisterDto.getPassword().equals(userRegisterDto.getMatchingPassword())) {
            throw new PasswordException("Password doesn't match!");
        }

        User user = new User();
        String encoded = bCryptPasswordEncoder.
                encode(userRegisterDto.getPassword());
        user.setPassword(encoded);
        user.setEmail(userRegisterDto.getEmail());
        user.setDateOfBirth(userRegisterDto.getDateOfBirth());
        user.setDateOfRegistration(LocalDateTime.now());
        user.setUserName(userRegisterDto.getUserName());

        long userId = userRepository.register(user);
        chatRepository.joinToChat(userId, 1);
        setUserRole(UserRole.ROLE_USER, userId);
        return getById(userId);
    }

    @Override
    public List<User> getAll(String currentUserEmail) throws EmptyResultDataAccessException {
        return userRepository.getAll();
    }

    private void setUserRole(UserRole role, long userId) throws SQLException {
        UserRoles userRole = new UserRoles();
        userRole.setUserId(userId);
        userRole.setRoleId(role.ordinal() + 1);
        userRepository.setUserRole(userRole);
    }

    public UserRole getUserRole(long userId) throws SQLException {
        return userRepository.getUserRole(userId);
    }

    public String setConfirmationToken(long userId) throws SQLException {
        ConfirmationToken confirmationToken = new ConfirmationToken();

        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setConfirmationToken(UUID.randomUUID().toString());
        confirmationToken.setUserId(userId);

        return confirmationTokenRepository.setConfirmationToken(confirmationToken);
    }

    public boolean emailExist(String email) throws EmptyResultDataAccessException {
        Optional<User> user = Optional.empty();
        try {
            user = userRepository.getByEmail(email);
        } finally {
            return user.isPresent();
        }
    }

    @Override
    public boolean confirmAccount(String token) throws SQLException, UserException {
        ConfirmationToken confirmationToken = confirmationTokenRepository.getConfirmationToken(token);
        Optional<User> userOptional = userRepository.getById(confirmationToken.getUserId());

        if (userOptional.isPresent()) {
            User user = userRepository.confirmEmail(userOptional.get().getId());
            confirmationTokenRepository.removeToken(confirmationToken.getId());
            return user.isEnabled();
        } else {
            return false;
        }
    }

    @Override
    public boolean checkTokenForResetPassword(String email, String token) throws SQLException {
        ConfirmationToken confirmationToken = confirmationTokenRepository.getConfirmationToken(token);
        Optional<User> userOptional = userRepository.getById(confirmationToken.getUserId());

        if (userOptional.isPresent() &&
                email.equals(userOptional.get().getEmail())) {
            confirmationTokenRepository.removeToken(confirmationToken.getId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) throws PasswordException {
        Optional<User> userOptional = getByEmail(resetPasswordDto.getEmail());

        if (userOptional.isPresent()) {
            if (!resetPasswordDto.getPassword().equals(resetPasswordDto.getMatchingPassword())) {
                throw new PasswordException("Password doesn't match!");
            }
            String encoded = bCryptPasswordEncoder.
                    encode(resetPasswordDto.getPassword());

            userRepository.changePassword(userOptional.get().getId(), encoded);
        }
    }
}
