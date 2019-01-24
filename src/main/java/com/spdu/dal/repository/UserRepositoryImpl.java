package com.spdu.dal.repository;

import com.spdu.dal.mappers.UserMapper;
import com.spdu.model.entities.User;
import com.spdu.model.entities.relations.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long register(User user) throws SQLException {
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
            ps.setString(6, user.getPassword().trim());
            ps.setString(7, user.getEmail());
            return ps;
        }, keyHolder);

        return Long.valueOf(keyHolder.getKeys().get("id").toString());
    }

    @Override
    public long setUserRole(UserRoles userRole) throws SQLException {
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
        try {
            String query = "SELECT * FROM db_users WHERE id =" + id;

            User user = jdbcTemplate.queryForObject(query,
                    new Object[]{},
                    new UserMapper());

            return Optional.of(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAll() {
        return null;
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
}
