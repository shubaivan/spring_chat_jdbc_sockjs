package com.spdu.bll.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.sql.Time;

public class ChatTyping {
    private Integer userId;
    private Integer chatId;

    @JsonCreator
    public ChatTyping(
            @JsonProperty("userId") Integer userId,
            @JsonProperty("chatId") Integer chatId
    ) {
        this.userId = userId;
        this.chatId = chatId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
}

