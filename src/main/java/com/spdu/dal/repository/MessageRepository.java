package com.spdu.dal.repository;

import com.spdu.domain_models.entities.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    Optional<Message> getById(long id);

    List<Message> getByChatId(long id);

    long create(Message message);
}
