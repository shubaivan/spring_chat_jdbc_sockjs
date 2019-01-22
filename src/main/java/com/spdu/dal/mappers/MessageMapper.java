package com.spdu.dal.mappers;

import com.spdu.model.entities.Message;
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
        message.setDateOfCreated(rs.getTimestamp("date_of_created").toLocalDateTime());
        message.setText(rs.getString("text"));
        return message;
    }
}
