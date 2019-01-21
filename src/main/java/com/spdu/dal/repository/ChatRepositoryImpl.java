package com.spdu.dal.repository;

import com.spdu.dal.mappers.ChatMapper;
import com.spdu.model.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

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
    public Optional<Chat> getById(long id) {
        try {
            String query = "SELECT * FROM chats WHERE chats.id =" + id;

            Chat chat = jdbcTemplate.queryForObject(query,
                    new Object[]{},
                    new ChatMapper());

            return Optional.of(chat);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Chat> create(Chat chat) throws SQLException {
        String query = "INSERT INTO chats (" +
                "chat_type, date_of_created," +
                " description, name," +
                "tags, owner_id) VALUES (?,?,?,?,?,?)";

        jdbcTemplate.update(query, chat.getChatType(), LocalDateTime.now(),
                chat.getDescription(), chat.getName(),
                chat.getTags(), chat.getOwnerId());
        return Optional.of(chat);
    }
}