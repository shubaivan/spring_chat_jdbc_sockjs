package com.spdu.bll.interfaces;

import com.spdu.bll.exceptions.UserException;
import com.spdu.bll.models.UserRegisterDTO;
import com.spdu.domain_models.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getById(long id);

    Optional<User> register(UserRegisterDTO userRegisterDTO) throws UserException;

    List<User> getAll(String currentUserEmail);

    Optional<User> getByEmail(String email);
}
