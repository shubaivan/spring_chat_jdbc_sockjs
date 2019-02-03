package com.spdu.dal.repository;

import com.spdu.bll.models.constants.UserRole;
import com.spdu.domain_models.entities.User;
import com.spdu.domain_models.entities.relations.UserRoles;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getById(long id) throws SQLException;

    List<User> getAll();

    long register(User user) throws SQLException;

    void setUserRole(UserRoles userRole) throws SQLException;

    Optional<User> getByEmail(String email);

    public Optional<User> getByUserName(String userName);

    UserRole getUserRole(long userId);
}
