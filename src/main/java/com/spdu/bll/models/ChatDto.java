package com.spdu.bll.models;

import com.spdu.bll.models.constants.ChatType;
import com.spdu.domain_models.entities.Chat;

public class ChatDto {
    private String name;
    private long id;
    private ChatType chatType;
    private String tags;
    private String description;
    private long ownerId;
    private long appendUserId;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getAppendUserId() {
        return appendUserId;
    }

    public void setAppendUserId(long appendUserId) {
        this.appendUserId = appendUserId;
    }
}