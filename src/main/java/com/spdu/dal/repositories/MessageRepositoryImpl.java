package com.spdu.dal.repositories;

import com.spdu.bll.custom_exceptions.MessageException;
import com.spdu.dal.mappers.MessageMapper;
import com.spdu.domain_models.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
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
    public Message update(long id, Message message) throws SQLException, MessageException {
        String query = "UPDATE messages SET text = ? WHERE id = ?";
        jdbcTemplate.update(query, message.getText(), id);
        Optional<Message> modifiedMessage = getById(id);
        if (modifiedMessage.isPresent()) {
            return modifiedMessage.get();
        } else {
            throw new MessageException("Can't update message with id: " + message.getId());
        }
    }

    @Override
    public List<Message> getMessages(long id, Optional<String> keyword) throws EmptyResultDataAccessException {
        String query = "SELECT *, \n" +
                "  CASE\n" +
                "    WHEN concat(u.first_name, ' ', u.last_name)=' ' THEN u.user_name\n" +
                "    ELSE concat(u.first_name, ' ', u.last_name)\n" +
                "  END \n" +
                "  AS fullname\n" +
                "\n" +
                "FROM messages AS m \n" +
                "LEFT JOIN db_users AS u ON u.id = m.author_id \n" +
                "WHERE m.chat_id =? \n" +
                "ORDER BY m.id ASC";

        Object[] parameters = new Object[]{id};

        if (keyword.isPresent()) {
            query += " AND m.text ILIKE ?";
            parameters = new Object[]{id, "%" + keyword.get() + "%"};
        }

        List<Message> messages = jdbcTemplate.query(query,
                parameters,
                (ResultSetExtractor<List<Message>>) rs -> toMessagesList(rs));
        return messages;
    }

    private List<Message> toMessagesList(ResultSet rs) throws SQLException {
        List<Message> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new MessageMapper().mapRow(rs, rs.getRow()));
        }
        return list;
    }

    @Override
    public long create(Message message) throws SQLException {
        String query = "INSERT INTO messages (" +
                "text, created_at," +
                " author_id, relative_message_id," +
                "relative_chat_id, chat_id, message_type) VALUES (?,?,?,?,?,?,?)";

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
            ps.setString(7, message.getMessageType().name());
            return ps;
        }, keyHolder);
        return Long.valueOf(keyHolder.getKeys().get("id").toString());
    }
}
