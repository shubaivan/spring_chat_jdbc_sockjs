package com.spdu.bll.models.sockets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageEdit {
    private long chatId;
    private long messageId;
    private String newContent;

    @JsonCreator
    public MessageEdit(@JsonProperty("chatId") long chatId,
                       @JsonProperty("messageId") long messageId,
                       @JsonProperty("newContent") String newContent) {
        this.chatId = chatId;
        this.messageId = messageId;
        this.newContent = newContent;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }
}
