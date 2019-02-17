package com.spdu.domain_models.entities.relations;

import java.time.LocalDateTime;

public class ChatsUsers {
    private long id;
    private long chatId;
    private long userId;
    private LocalDateTime dateOfJoined;

    public ChatsUsers() {
    }

    public long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getDateOfJoined() {
        return dateOfJoined;
    }

    public void setDateOfJoined(LocalDateTime dateOfJoined) {
        this.dateOfJoined = dateOfJoined;
    }
}
