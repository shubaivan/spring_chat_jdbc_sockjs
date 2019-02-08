package com.spdu.dal.repositories;

import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.models.constants.UserRole;
import com.spdu.domain_models.entities.User;
import com.spdu.domain_models.entities.relations.UserRoles;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getById(long id) throws EmptyResultDataAccessException;

    List<User> getAll() throws EmptyResultDataAccessException;

    long register(User user) throws SQLException;

    void setUserRole(UserRoles userRole) throws SQLException;

    Optional<User> getByEmail(String email) throws EmptyResultDataAccessException;

    Optional<User> getByUserName(String userName) throws EmptyResultDataAccessException;

    UserRole getUserRole(long userId) throws SQLException;

    User update(long id, User user) throws SQLException, UserException;

    User updateAvatar(long id, long fileId) throws SQLException, UserException;
}
