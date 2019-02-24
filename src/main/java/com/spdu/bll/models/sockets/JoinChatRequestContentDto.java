package com.spdu.bll.models.sockets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinChatRequestContentDto {

    private Integer chatId;

    @JsonCreator
    public JoinChatRequestContentDto(@JsonProperty("chatId") Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getId() {
        return chatId;
    }

    public void setId(Integer id) {
        this.chatId = id;
    }
}
