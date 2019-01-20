package com.spdu.dal.repository;

import com.spdu.model.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ChatRepositoryImpl implements ChatRepository {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Chat getById(long id) {
        try {
            String query = "SELECT * FROM chat WHERE chat.id =" + id;

            Chat chat = jdbcTemplate.queryForObject(query,
                    new Object[]{},
                    (rs, rowNum) -> {
                        Chat baseChat = new Chat();
                        rs.getLong("id");
                        baseChat.setName(rs.getString("name"));
                        return baseChat;
                    });
            return chat;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}