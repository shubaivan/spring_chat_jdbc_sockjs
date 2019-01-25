package com.spdu.dal.repository;

import com.spdu.model.constants.UserRole;
import com.spdu.model.entities.User;
import com.spdu.model.entities.relations.UserRoles;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getById(long id) throws SQLException;

    List<User> getAll();

    long register(User user) throws SQLException;

    long setUserRole(UserRoles userRole) throws SQLException;

    Optional<User> getByEmail(String email);

    UserRole getUserRole(long userId);
}
