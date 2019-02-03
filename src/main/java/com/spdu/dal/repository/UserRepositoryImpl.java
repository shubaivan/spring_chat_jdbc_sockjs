package com.spdu.dal.repository;

import com.spdu.dal.mappers.UserMapper;
import com.spdu.bll.models.constants.UserRole;
import com.spdu.domain_models.entities.User;
import com.spdu.domain_models.entities.relations.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long register(User user) {
        String query = "INSERT INTO db_users (" +
                "date_of_birth, date_of_registration," +
                "first_name, last_name," +
                "user_name, password," +
                "email) VALUES (?,?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(user.getDateOfBirth()));
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());
            ps.setString(5, user.getUserName());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getEmail());
            return ps;
        }, keyHolder);
        return Long.valueOf(keyHolder.getKeys().get("id").toString());
    }

    @Override
    public long setUserRole(UserRoles userRole) {
        String query = "INSERT INTO user_roles (" +
                "user_id, role_id) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userRole.getUserId());
            ps.setLong(2, userRole.getRoleId());
            return ps;
        }, keyHolder);
        return Long.valueOf(keyHolder.getKeys().get("id").toString());
    }

    @Override
    public Optional<User> getById(long id) {
        String query = "SELECT * FROM db_users WHERE id =?";
        User user = jdbcTemplate.queryForObject(query,
                new Object[]{id},
                new UserMapper());
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT * FROM db_users ";
        List<User> users = jdbcTemplate.query(query,
                rs -> {
                    List<User> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(new UserMapper().mapRow(rs, rs.getRow()));
                    }
                    return list;
                });
        return users;
    }

    @Override
    public Optional<User> getByEmail(String email) {
        try {
            String query = "SELECT * FROM db_users WHERE email=?";
            User user = jdbcTemplate.queryForObject(query,
                    new Object[]{email},
                    new UserMapper());
            return Optional.of(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public UserRole getUserRole(long userId) {
        String query = "SELECT name FROM roles " +
                "JOIN user_roles on user_roles.role_id = roles.id " +
                "WHERE user_roles.user_id=?";
        String roleName = jdbcTemplate.queryForObject(query,
                new Object[]{userId}, String.class);
        return UserRole.valueOf(roleName);
    }
}