package com.spdu.dal.mappers;

import com.spdu.model.constants.ChatType;
import com.spdu.model.entities.Chat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatMapper implements RowMapper<Chat> {

    @Override
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
        Chat chat = new Chat();
        chat.setName(rs.getString("name"));
        chat.setDescription(rs.getString("description"));
        chat.setDateOfCreated(rs.getTimestamp("date_of_created").toLocalDateTime());
        chat.setChatType(ChatType.values()[rs.getInt("chat_type")]);
        chat.setTags(rs.getString("tags"));
        chat.setId(rs.getLong("id"));
        chat.setOwnerId(rs.getLong("owner_id"));
        return chat;
    }
}
