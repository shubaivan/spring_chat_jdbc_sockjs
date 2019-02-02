package com.spdu.bll.interfaces;

import com.spdu.bll.exceptions.UserException;
import com.spdu.bll.models.UserRegisterDto;
import com.spdu.domain_models.entities.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getById(long id) throws SQLException;

    Optional<User> register(UserRegisterDto userRegisterDTO) throws UserException, SQLException;

    List<User> getAll(String currentUserEmail);

    Optional<User> getByEmail(String email);
}
