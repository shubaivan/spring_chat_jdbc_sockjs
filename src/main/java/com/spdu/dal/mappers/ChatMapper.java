package com.spdu.dal.mappers;

import com.spdu.bll.models.constants.ChatType;
import com.spdu.domain_models.entities.Chat;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatMapper implements RowMapper<Chat> {

    @Override
    public Chat mapRow(ResultSet rs, int rowNum) throws SQLException {
        Chat chat = new Chat();
        chat.setName(rs.getString("name"));
        chat.setDescription(rs.getString("description"));
        chat.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        chat.setChatType(ChatType.values()[rs.getInt("chat_type")]);
        chat.setTags(rs.getString("tags"));
        chat.setId(rs.getLong("id"));
        chat.setOwnerId(rs.getLong("owner_id"));
        return chat;
    }
}
