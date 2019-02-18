package com.spdu.bll.interfaces;

import com.spdu.bll.custom_exceptions.PasswordException;
import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.models.ResetPasswordDto;
import com.spdu.bll.models.UserDto;
import com.spdu.bll.models.UserRegisterDto;
import com.spdu.bll.models.constants.UserRole;
import com.spdu.domain_models.entities.User;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getById(long id) throws EmptyResultDataAccessException;

    List<User> getByChatId(long id) throws EmptyResultDataAccessException;

    Optional<User> register(UserRegisterDto userRegisterDTO) throws UserException, SQLException, PasswordException;

    List<User> getAll(String currentUserEmail) throws EmptyResultDataAccessException;

    Optional<User> getByEmail(String email) throws EmptyResultDataAccessException;

    Optional<User> getByUserName(String userName) throws EmptyResultDataAccessException;

    User update(long id, UserDto user) throws SQLException, UserException;

    UserDto updateAvatar(long id, long fileId) throws SQLException, UserException;

    String setConfirmationToken(long userId) throws SQLException;

    boolean confirmAccount(String token) throws SQLException, UserException;

    boolean checkTokenForResetPassword(String email, String token) throws SQLException;

    void resetPassword(ResetPasswordDto resetPasswordDto) throws UserException, PasswordException;

    UserRole getUserRole(long userId) throws SQLException;

    boolean emailExist(String email) throws EmptyResultDataAccessException;
}
