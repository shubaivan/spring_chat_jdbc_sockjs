package com.spdu.bll.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JoinChatRequestContentDTO {

    private Integer chatId;

    @JsonCreator
    public JoinChatRequestContentDTO(@JsonProperty("chatId") Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getId() {
        return chatId;
    }

    public void setId(Integer id) {
        this.chatId = id;
    }
}
