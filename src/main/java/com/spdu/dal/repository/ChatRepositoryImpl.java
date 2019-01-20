package com.spdu.dal.repository;

import com.spdu.model.constants.ChatType;
import com.spdu.model.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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
                    (rs, rowNum) -> {
                        Chat baseChat = new Chat();

                        baseChat.setName(rs.getString("name"));
                        baseChat.setDescription(rs.getString("description"));
                        baseChat.setDateOfCreated(rs.getTimestamp("date_of_created").toLocalDateTime());
                        baseChat.setChatType(ChatType.values()[rs.getInt("type")]);
                        baseChat.setTags(rs.getString("tags"));
                        baseChat.setId(rs.getLong("id"));
                        return baseChat;
                    });
            return Optional.of(chat);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}