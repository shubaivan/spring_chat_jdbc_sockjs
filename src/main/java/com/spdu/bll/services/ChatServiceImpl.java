package com.spdu.bll.services;

import com.spdu.bll.custom_exceptions.ChatException;
import com.spdu.bll.interfaces.ChatService;
import com.spdu.bll.models.ChatDto;
import com.spdu.dal.repositories.ChatRepository;
import com.spdu.domain_models.entities.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {

    @Override
    public ChatDto update(long id, ChatDto chatDto) throws SQLException, ChatException {
        Optional<Chat> optionalChat = getById(id);
        if (optionalChat.isPresent()) {
            Chat oldChat = optionalChat.get();
            oldChat.setName(chatDto.getName());
            oldChat.setDescription(chatDto.getDescription());
            oldChat.setTags(chatDto.getTags());
            Chat modifiedChat = chatRepository.update(id, oldChat);
            return new ChatDto(modifiedChat);
        } else {
            throw new ChatException("Chat not found");
        }
    }

    private final ChatRepository chatRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Optional<Chat> getById(long id) throws EmptyResultDataAccessException {
        return chatRepository.getById(id);
    }

    @Override
    public Optional<Chat> create(ChatDto chatDto) throws SQLException {
        if (chatDto == null) {
            throw new RuntimeException("Chat is empty!");
        }
        Chat existChat = null;
        Chat chat = null;
        if (chatDto.getAppendUserId() != 0) {
            Chat checkChat = this.userIsPresentInOwnerPrivateChat(
                    chatDto.getOwnerId(),
                    chatDto.getAppendUserId()
            );

            if (checkChat != null) {
                existChat = checkChat;
            }
        }
        long chatId;
        if (existChat == null) {
            chat = new Chat();

            chat.setTags(chatDto.getTags());
            chat.setOwnerId(chatDto.getOwnerId());
            chat.setDescription(chatDto.getDescription());
            chat.setName(chatDto.getName());
            chat.setCreatedAt(LocalDateTime.now());
            chat.setChatType(chatDto.getChatType());

            chatId = chatRepository.create(chat);

            joinToChat(chat.getOwnerId(), chatId);
        } else {
            chat = existChat;
            chatId = chat.getId();
        }

        if (chatDto.getAppendUserId() != 0) {
            joinToChat(chatDto.getAppendUserId(), chatId);
        }

        return getById(chatId);
    }

    @Override
    public boolean joinToChat(long userId, long chatId) {
        if (userIsPresentInChat(userId, chatId)) {
            return false;
        }
        long result = chatRepository.joinToChat(userId, chatId);

        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean userIsPresentInChat(long userId, long chatId) throws EmptyResultDataAccessException {
        return chatRepository.userIsPresentInChat(userId, chatId);
    }

    private Chat userIsPresentInOwnerPrivateChat(
            long userId, long chatId
    ) throws EmptyResultDataAccessException {
        List<Chat> result = this.getChatRepository()
                .userIsPresentInOwnerPrivateChat(userId, chatId);
        if (result.size() > 0) {
            return result.get(0);
        }

        return null;
    }

    @Override
    public List<Chat> getAll(long userId) throws EmptyResultDataAccessException {
        return chatRepository.getAll(userId);
    }

    @Override
    public List<Chat> getAllOwn(long userId) throws EmptyResultDataAccessException {
        return chatRepository.getAllOwn(userId);
    }

    @Override
    public List<Chat> getPublic(long userId) throws EmptyResultDataAccessException {
        return chatRepository.getPublic(userId);
    }

    @Override
    public List<Chat> getPrivate(long userId) throws EmptyResultDataAccessException {
        return chatRepository.getAllPrivate(userId);
    }

    private ChatRepository getChatRepository() {
        return chatRepository;
    }
}
