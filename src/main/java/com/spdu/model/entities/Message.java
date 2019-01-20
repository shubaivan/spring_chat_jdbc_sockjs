package com.spdu.model.entities;

import java.time.LocalDateTime;
import java.util.List;

public class Message {
    private long id;
    private Chat chat;
    private User user;
    private String text;
    private LocalDateTime dateOfCreated;
    private long relativeMessageId;
    private long relativeChatId;
    private List<FavoriteMessage> favoriteMessages;

    public Message() {
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

    public LocalDateTime getDateOfCreated() {
        return dateOfCreated;
    }

    public void setDateOfCreated(LocalDateTime dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public List<FavoriteMessage> getFavoriteMessages() {
        return favoriteMessages;
    }

    public void setFavoriteMessages(List<FavoriteMessage> favoriteMessages) {
        this.favoriteMessages = favoriteMessages;
    }
}
