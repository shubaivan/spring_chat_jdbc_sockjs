package com.spdu.bll.models;

import com.spdu.domain_models.entities.Message;

import java.time.LocalDateTime;

public class MessageDto {
    private String userName;
    private String content;
    private LocalDateTime date;
    private long id;
    private long authorId;

    public MessageDto() {
    }

    public MessageDto(Message message) {
        this.authorId = message.getAuthorID();
        this.id = message.getId();
        this.userName = message.getFullName();
        this.content = message.getText();
        this.date = message.getCreatedAt();
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
