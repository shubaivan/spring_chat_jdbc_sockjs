package com.spdu.domain_models.entities;

import com.spdu.bll.models.constants.ChatType;

import java.time.LocalDateTime;

public class Chat {
    private long id;
    private String name;
    private LocalDateTime createdAt;
    private ChatType chatType;
    private String tags;
    private String description;
    private long ownerId;

    private long checkExist;

    public Chat() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public long getCheckExist() {
        return checkExist;
    }

    public void setCheckExist(long checkExist) {
        this.checkExist = checkExist;
    }
}
