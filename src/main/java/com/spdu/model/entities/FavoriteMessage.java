package com.spdu.model.entities;

import java.time.LocalDateTime;

public class FavoriteMessage {
    private long id;
    private String text;
    private String userName;
    private LocalDateTime dateOfCreated;

    public FavoriteMessage() {
    }

    public long getId() {
        return id;
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
}
