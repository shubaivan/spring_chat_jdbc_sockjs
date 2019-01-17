package com.spdu.model.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favoriteMessages")
public class FavoriteMessage {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "chatId")
    private long chatId;

    @Column(name = "userId")
    private long userId;

    @Column(name = "text")
    private String text;

    @Column(name = "userName")
    private String userName;

    @Column(name = "dateOfCreated")
    private LocalDateTime dateOfCreated;

    @Column(name = "messageId")
    private long messageId;

    public FavoriteMessage() {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getDateOfCreated() {
        return dateOfCreated;
    }

    public void setDateOfCreated(LocalDateTime dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
}
