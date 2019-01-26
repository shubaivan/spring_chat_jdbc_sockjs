package com.spdu.dal.repository;

import com.spdu.dal.mappers.ChatMapper;
import com.spdu.model.constants.ChatType;
import com.spdu.model.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Repository
public class ChatRepositoryImpl implements ChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChatRepositoryImpl(JdbcTemplate jdbcTemplate) {
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

    @Override
    public long joinToChat(long userId, long chatId) throws SQLException {
        String query = "INSERT INTO chats_users (" +
                "chat_id, user_id," +
                " date_of_joined) VALUES (?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, chatId);
            ps.setLong(2, userId);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);

        return Long.valueOf(keyHolder.getKeys().get("id").toString());
    }

    @Override
    public List<Chat> getAllOwn(long userId) {
        List<Chat> chats = new LinkedList<>();
        try {
            String query = "SELECT * FROM chats WHERE chats.owner_id=" + userId;
            chats = getByQuery(query);
            return chats;
        } catch (Exception e) {
            e.printStackTrace();
            return chats;
        }
    }

    @Override
    public List<Chat> getAll(long userId) {
        List<Chat> chats = new LinkedList<>();
        try {
            String query = "SELECT * FROM chats JOIN chats_users u on chats.id = u.chat_id" +
                    "  WHERE u.user_id=" + userId;
            chats = getByQuery(query);
            return chats;
        } catch (Exception e) {
            e.printStackTrace();
            return chats;
        }
    }

    private List<Chat> getByQuery(String query) {
        return jdbcTemplate.query(query,
                rs -> {
                    List<Chat> list = new LinkedList<>();
                    while (rs.next()) {
                        list.add(new ChatMapper().mapRow(rs, rs.getRow()));
                    }
                    return list;
                });
    }

    @Override
    public List<Chat> getPublic(long userId) {
        List<Chat> chats = new LinkedList<>();
        try {
            String query = "SELECT * FROM chats JOIN chats_users u on chats.id = u.chat_id  WHERE u.user_id <> " + userId +
                    " AND chats.chat_type=" + ChatType.PUBLIC.ordinal();

            chats = getByQuery(query);
            return chats;
        } catch (Exception e) {
            e.printStackTrace();
            return chats;
        }
    }
}