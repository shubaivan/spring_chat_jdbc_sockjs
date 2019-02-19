package com.spdu.bll.models;

import com.spdu.bll.models.constants.ChatType;
import com.spdu.domain_models.entities.Chat;

public class ChatDto {
    private String name;
    private ChatType chatType;
    private String tags;
    private String description;
    private long ownerId;

    public ChatDto() {
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public ChatDto(Chat chat) {
        this.name = chat.getName();
        this.chatType = chat.getChatType();
        this.tags = chat.getTags();
        this.description = chat.getDescription();
        this.ownerId = chat.getOwnerId();
    }

    public long getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}