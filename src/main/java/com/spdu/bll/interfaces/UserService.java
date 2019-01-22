package com.spdu.bll.interfaces;

import com.spdu.model.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getById(long id);
}
