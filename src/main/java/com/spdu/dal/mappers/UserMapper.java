package com.spdu.dal.mappers;

import com.spdu.model.entities.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setAvatar(rs.getLong("avatar"));
        user.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
        user.setDateOfRegistration(rs.getTimestamp("date_of_registration").toLocalDateTime());
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setUserName(rs.getString("user_name"));
        user.setPassword(rs.getString("password").trim());
        user.setEmail(rs.getString("email"));
        user.setUrlFacebook(rs.getString("url_facebook"));
        user.setUrlGit(rs.getString("url_git"));
        user.setUrlLinkedin(rs.getString("url_linkedin"));
        return user;
    }
}
