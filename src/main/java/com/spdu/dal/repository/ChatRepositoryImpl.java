package com.spdu.dal.repository;

import com.spdu.dal.mappers.ChatMapper;
import com.spdu.model.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
    public long create(Chat chat) throws SQLException {
        String query = "INSERT INTO chats (" +
                "chat_type, date_of_created," +
                " description, name," +
                "tags, owner_id) VALUES (?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, chat.getChatType().ordinal());
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(3, chat.getDescription());
            ps.setString(4, chat.getName());
            ps.setString(5, chat.getTags());
            ps.setLong(6, chat.getOwnerId());
            return ps;
        }, keyHolder);

        return Long.valueOf(keyHolder.getKeys().get("id").toString());
    }
}