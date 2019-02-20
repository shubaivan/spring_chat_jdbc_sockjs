package com.spdu.bll.interfaces;

import com.spdu.bll.models.MessageReturnDto;
import com.spdu.bll.models.MessagesRequestContentDto;
import com.spdu.domain_models.entities.Message;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    Optional<Message> getById(long id) throws EmptyResultDataAccessException;

//    List<Message> getByChatId(long id) throws EmptyResultDataAccessException;

    Optional<Message> create(Message message);

    Optional<MessageReturnDto> send(String userEmail, Message message);

//    List<Message> searchMessage(long id, String keyword) throws EmptyResultDataAccessException;

    List<Message> getMessages(MessagesRequestContentDto requestContentDTO) throws EmptyResultDataAccessException;
}
