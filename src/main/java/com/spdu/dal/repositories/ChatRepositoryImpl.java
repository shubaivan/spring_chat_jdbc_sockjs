package com.spdu.dal.repositories;

import com.spdu.bll.custom_exceptions.ChatException;
import com.spdu.bll.models.constants.ChatType;
import com.spdu.dal.mappers.ChatMapper;
import com.spdu.domain_models.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
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
    public Optional<Chat> getById(long id) throws EmptyResultDataAccessException {
        String query = "SELECT * FROM chats WHERE chats.id =?";

        Chat chat = jdbcTemplate.queryForObject(query,
                new Object[]{id},
                new ChatMapper());
        return Optional.of(chat);
    }

    @Override
    public Chat update(long id, Chat chat) throws SQLException, ChatException {
        String query = "UPDATE chats SET name = ?, tags = ?, description = ? WHERE id = ?";
        jdbcTemplate.update(query, chat.getName(), chat.getTags(), chat.getDescription(), id);
        Optional<Chat> modifiedChat = getById(id);
        if (modifiedChat.isPresent()) {
            return modifiedChat.get();
        } else {
            throw new ChatException("Can't update chat " + chat.getName());
        }
    }

    @Override
    public long create(Chat chat) throws SQLException {
        String query = "INSERT INTO chats (" +
                "chat_type, created_at," +
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
    public long joinToChat(long userId, long chatId) {
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
    public List<Chat> getAllOwn(long userId) throws EmptyResultDataAccessException {
        String query = "SELECT * FROM chats WHERE chats.owner_id=" + userId;
        return getByQuery(query);
    }

    @Override
    public boolean userIsPresentInChat(long userId, long chatId) throws EmptyResultDataAccessException {
        String query = "SELECT * FROM chats_users " +
                "WHERE chats_users.user_id=" + userId
                + "AND chats_users.chat_id=" + chatId;
        return jdbcTemplate.queryForRowSet(query).next();
    }

    public int removeChatUser(long userId, long chatId)
    {
        String query = "DELETE FROM chats_users \n" +
                "WHERE chat_id = ?\n" +
                "AND user_id = ?\n";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int count = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, chatId);
            ps.setLong(2, userId);

            return ps;
        }, keyHolder);

        return count;
    }

    @Override
    public List<Chat> getAll(long userId) throws EmptyResultDataAccessException {
        String query = "SELECT * FROM chats JOIN chats_users u on chats.id = u.chat_id" +
                "  WHERE u.user_id=" + userId + "\n" +
                " AND chats.owner_id != " + userId;
        return getByQuery(query);
    }

    private List<Chat> getByQuery(String query) throws EmptyResultDataAccessException {
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
    public List<Chat> getPublic(long userId) throws EmptyResultDataAccessException {
        String query = "SELECT * FROM chats AS rc\n" +
                "WHERE rc.chat_type="+ChatType.PUBLIC.ordinal()+"\n" +
                "AND rc.id NOT IN (SELECT cu.chat_id FROM db_users as u\n" +
                "INNER JOIN chats_users AS cu ON cu.user_id = u.id\n" +
                "LEFT JOIN chats AS ch ON ch.id = cu.chat_id\n" +
                "WHERE cu.user_id ="+userId+"\n" +
                "GROUP BY cu.chat_id)";

        return getByQuery(query);
    }
}