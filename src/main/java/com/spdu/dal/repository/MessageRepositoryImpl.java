package com.spdu.dal.repository;

import com.spdu.dal.mappers.MessageMapper;
import com.spdu.domain_models.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class MessageRepositoryImpl implements MessageRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MessageRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Message> getById(long id) throws EmptyResultDataAccessException {
        String query = "SELECT * FROM messages WHERE messages.id =?";
        Message message = jdbcTemplate.queryForObject(query,
                new Object[]{id},
                new MessageMapper());
        return Optional.of(message);
    }

    @Override
    public List<Message> getByChatId(long id) throws EmptyResultDataAccessException {
        String query = "SELECT * FROM messages WHERE messages.chat_id =?";
        List<Message> messages = jdbcTemplate.query(query,
                new Object[]{id},
                rs -> {
                    List<Message> list = new LinkedList<>();
                    while (rs.next()) {
                        list.add(new MessageMapper().mapRow(rs, rs.getRow()));
                    }
                    return list;
                });
        return messages;
    }

    @Override
    public long create(Message message) throws SQLException {
        String query = "INSERT INTO messages (" +
                "text, created_at," +
                " author_id, relative_message_id," +
                "relative_chat_id, chat_id) VALUES (?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, message.getText());
            ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            ps.setLong(3, message.getAuthorID());
            ps.setLong(4, message.getRelativeMessageId());
            ps.setLong(5, message.getRelativeChatId());
            ps.setLong(6, message.getChatId());
            return ps;
        }, keyHolder);
        return Long.valueOf(keyHolder.getKeys().get("id").toString());
    }
}
