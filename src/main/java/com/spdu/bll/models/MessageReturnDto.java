package com.spdu.bll.models;

import java.time.LocalDateTime;

public class MessageReturnDto {
    private String userName;
    private String content;
    private LocalDateTime date;
    private long id;

    public MessageReturnDto() {
    }

    public MessageReturnDto(String userName, String content, LocalDateTime date, long id) {
        this.userName = userName;
        this.content = content;
        this.date = date;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
