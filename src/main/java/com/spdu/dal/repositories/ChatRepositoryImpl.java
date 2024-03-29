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
    public Optional<Chat> getById(long id) {
        try {
            String query = "SELECT * FROM chats WHERE chats.id =?";

            Chat chat = jdbcTemplate.queryForObject(query,
                    new Object[]{id},
                    new ChatMapper());
            return Optional.of(chat);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Chat update(long id, Chat chat) throws ChatException {
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
    public long create(Chat chat) {
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
    public List<Chat> getAllOwn(long userId) {
        String query = "SELECT * FROM chats " +
                "WHERE chats.owner_id=" + userId +
                " AND chats.chat_type != " + ChatType.PRIVATE.ordinal();
        return getByQuery(query);
    }

    @Override
    public boolean userIsPresentInChat(long userId, long chatId) {
        String query = "SELECT * FROM chats_users " +
                "WHERE chats_users.user_id=" + userId
                + "AND chats_users.chat_id=" + chatId;
        return jdbcTemplate.queryForRowSet(query).next();
    }

    @Override
    public List<Chat> userIsPresentInOwnerPrivateChat(long ownerId, long appendUserId) {
        String query = "SELECT * \n" +
                "FROM chats as c\n" +
                "WHERE c.chat_type = " + ChatType.PRIVATE.ordinal() + "\n" +
                "AND c.owner_id = " + ownerId + "\n" +
                "AND c.id IN (SELECT cu.chat_id FROM db_users as u\n" +
                "                INNER JOIN chats_users AS cu ON cu.user_id = u.id\n" +
                "                LEFT JOIN chats AS ch ON ch.id = cu.chat_id\n" +
                "                WHERE cu.user_id =" + appendUserId + "\n" +
                "\t\t\t\t\t\t\t\tAND ch.chat_type = " + ChatType.PRIVATE.ordinal() + "\n" +
                "                GROUP BY cu.chat_id\n" +
                ")\n" +
                "LIMIT 1";
        return getByQuery(query);
    }

    public int removeChatUser(long userId, long chatId) {
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
    public int removeChat(long chatId) {
        String query = "DELETE FROM chats " +
                "WHERE id = ?";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int count = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, chatId);
            return ps;
        }, keyHolder);

        return count;
    }

    @Override
    public boolean isOwnChat(long chatId, long userId) {
        String query = "SELECT * FROM chats " +
                "WHERE id=?"
                + " AND owner_id=?";

        return jdbcTemplate.queryForRowSet(query,
                new Object[]{chatId, userId}).next();
    }

    @Override
    public List<Chat> getAll(long userId) {
        String query = "SELECT * FROM chats JOIN chats_users u on chats.id = u.chat_id" +
                "  WHERE u.user_id=" + userId + "\n" +
                " AND chats.owner_id != " + userId +
                " AND chats.chat_type != " + ChatType.PRIVATE.ordinal();

        return getByQuery(query);
    }

    @Override
    public List<Chat> getAllPrivate(long userId) {
        String query = "SELECT * FROM chats JOIN chats_users u on chats.id = u.chat_id" +
                "  WHERE u.user_id=" + userId + "\n" +
                " AND chats.chat_type = " + ChatType.PRIVATE.ordinal();

        return getByQuery(query);
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
        String query = "SELECT * FROM chats AS rc\n" +
                "WHERE rc.id NOT IN " +
                "(SELECT cu.chat_id FROM db_users as u\n" +
                "INNER JOIN chats_users AS cu ON cu.user_id = u.id\n" +
                "LEFT JOIN chats AS ch ON ch.id = cu.chat_id\n" +
                "WHERE cu.user_id =" + userId + "\n" +
                "GROUP BY cu.chat_id)" + "\n" +
                "AND rc.chat_type != " + ChatType.PRIVATE.ordinal();

        return getByQuery(query);
    }
}