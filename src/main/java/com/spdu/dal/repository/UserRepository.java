package com.spdu.dal.repository;

import com.spdu.model.entities.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getById(long id) throws SQLException;

    List<User> getAll();
}
