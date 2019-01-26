package com.spdu.bll.models;

import java.time.LocalDateTime;

public class MessageReturnDTO {
    private String userName;
    private String content;
    private LocalDateTime date;

    public MessageReturnDTO() {
    }

    public MessageReturnDTO(String userName, String content, LocalDateTime date) {
        this.userName = userName;
        this.content = content;
        this.date = date;
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
