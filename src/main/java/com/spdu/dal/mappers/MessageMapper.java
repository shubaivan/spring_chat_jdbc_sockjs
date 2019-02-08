package com.spdu.dal.mappers;

import com.spdu.bll.models.MessageType;
import com.spdu.domain_models.entities.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message message = new Message();
        message.setId(rs.getLong("id"));
        message.setAuthorID(rs.getLong("author_id"));
        message.setChatId(rs.getLong("chat_id"));
        message.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        message.setText(rs.getString("text"));

        if (rs.getString("message_type") != null) {
            message.setMessageType(MessageType.valueOf(rs.getString("message_type")));
        }

        return message;
    }
}
