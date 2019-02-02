package com.spdu.dal.mappers;

import com.spdu.domain_models.entities.Message;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageMapper implements RowMapper<Message> {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message message = new Message();
        message.setId(rs.getLong("id"));
        message.setAuthorID(rs.getLong("author_id"));
        message.setChatId(rs.getLong("chat_id"));
        message.setDateOfCreated(rs.getTimestamp("created_at").toLocalDateTime());
        message.setText(rs.getString("text"));
        return message;
    }
}
