package com.spdu.bll.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessagesRequestContentDTO {

    private Integer chatId;

    private String keyword;

    @JsonCreator
    public MessagesRequestContentDTO(
            @JsonProperty("chatId") Integer chatId,
            @JsonProperty("keyword") String keyword
    ) {
        if (keyword != null && keyword.length()>0) {
            this.keyword = keyword;
        }
        this.chatId = chatId;
    }

    public Integer getId() {
        return chatId;
    }

    public void setId(Integer id) {
        this.chatId = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
