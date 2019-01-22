package com.spdu.model.entities;

import java.time.LocalDateTime;
import java.util.List;

public class Message {
    private long id;
    private String text;
    private LocalDateTime dateOfCreated;
    private long relativeMessageId;
    private long relativeChatId;

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
}
