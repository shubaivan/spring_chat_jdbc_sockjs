package com.spdu.bll.models;

public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private Integer chatId;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }
}

