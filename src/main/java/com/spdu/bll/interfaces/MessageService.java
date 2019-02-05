package com.spdu.bll.interfaces;

import com.spdu.bll.models.MessageReturnDto;
import com.spdu.domain_models.entities.Message;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MessageService {
    Optional<Message> getById(long id);

    List<Message> getByChatId(long id);

    Optional<Message> create(Message message);

    Optional<MessageReturnDto> send(String userEmail, Message message);
}
