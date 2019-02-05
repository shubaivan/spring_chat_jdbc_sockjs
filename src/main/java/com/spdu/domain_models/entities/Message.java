package com.spdu.domain_models.entities;

import java.time.LocalDateTime;

public class Message {
    private long id;
    private String text;
    private LocalDateTime createdAt;
    private long authorID;
    private long relativeMessageId;
    private long relativeChatId;
    private long chatId;

    public Message() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(long authorID) {
        this.authorID = authorID;
    }

    public String getText() {
        return text;
    }

    public long getRelativeChatId() {
        return relativeChatId;
    }

    public void setRelativeChatId(long relativeChatId) {
        this.relativeChatId = relativeChatId;
    }

    public long getRelativeMessageId() {
        return relativeMessageId;
    }

    public void setRelativeMessageId(long relativeMessageId) {
        this.relativeMessageId = relativeMessageId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getId() {
        return id;
    }
}
