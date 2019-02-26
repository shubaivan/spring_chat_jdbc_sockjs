package com.spdu.dal.repositories;

import com.spdu.bll.custom_exceptions.MessageException;
import com.spdu.bll.models.MessageDto;
import com.spdu.domain_models.entities.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    Optional<Message> getById(long id);

    long create(Message message);

    List<Message> getMessages(long id, Optional<String> keyword);

    Message update(long id, Message message) throws MessageException;

    boolean updateOptimization(MessageDto message) throws MessageException;

    int removeMessage(long id);

    boolean isOwnMessage(long id, long userId);
}
