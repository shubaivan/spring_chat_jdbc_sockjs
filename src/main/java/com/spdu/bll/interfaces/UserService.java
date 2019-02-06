package com.spdu.bll.interfaces;

import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.models.UserDto;
import com.spdu.bll.models.UserRegisterDto;
import com.spdu.domain_models.entities.User;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getById(long id) throws EmptyResultDataAccessException;

    Optional<User> register(UserRegisterDto userRegisterDTO) throws UserException, SQLException;

    List<User> getAll(String currentUserEmail) throws EmptyResultDataAccessException;

    Optional<User> getByEmail(String email) throws EmptyResultDataAccessException;

    Optional<User> getByUserName(String userName) throws EmptyResultDataAccessException;

    UserDto update(long id, UserDto user) throws SQLException, UserException;
}
