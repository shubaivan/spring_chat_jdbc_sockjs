package com.spdu.dal.repository;

import com.spdu.dal.mappers.MessageMapper;
import com.spdu.model.entities.Message;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MessageRepositoryImpl implements MessageRepository {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MessageRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Message> getById(long id) throws SQLException {
        try {
            String query = "SELECT * FROM messages WHERE messages.id =" + id;

            Message message = jdbcTemplate.queryForObject(query,
                    new Object[]{},
                    new MessageMapper());

            return Optional.of(message);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Message> getByChatId(long id) throws SQLException {
        String query = "SELECT * FROM messages WHERE messages.chat_id =" + id;
        return getMessagesList(query);
    }

    @Override
    public List<Message> getAllMessages() {
        String query = "SELECT * FROM messages";
        return getMessagesList(query);
    }

    private List<Message> getMessagesList(String query) {
        List<Message> messages = jdbcTemplate.query(query,
                rs -> {
                    List<Message> list = new ArrayList<Message>();
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
                "text, date_of_created," +
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
