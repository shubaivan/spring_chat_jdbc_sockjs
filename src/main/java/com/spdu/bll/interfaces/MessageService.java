package com.spdu.bll.interfaces;

import com.spdu.model.entities.Message;

import java.util.Optional;

public interface MessageService {
    Optional<Message> getById(long id);

    Optional<Message> create(Message message);
}
