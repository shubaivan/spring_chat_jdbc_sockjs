package com.spdu.bll.interfaces;

import com.spdu.bll.custom_exceptions.MessageException;
import com.spdu.bll.models.MessageDto;
import com.spdu.bll.models.MessageReturnDto;
import com.spdu.bll.models.MessagesRequestContentDto;
import com.spdu.domain_models.entities.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    Optional<Message> getById(long id);

    Optional<Message> create(Message message);

    Optional<MessageReturnDto> send(String userEmail, Message message);

    List<Message> getMessages(MessagesRequestContentDto requestContentDTO);

    MessageDto update(long id, MessageDto messageDto) throws MessageException;

    MessageDto updateOptimization(MessageDto messageDto) throws MessageException;

    boolean removeMessage(long chatId, long ownerId) throws MessageException;
}
