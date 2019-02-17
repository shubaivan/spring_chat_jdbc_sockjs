package com.spdu.domain_models.entities;

import com.spdu.bll.models.MessageType;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

public class Message {
    private long id;
    private String text;
    private LocalDateTime createdAt;
    private long authorID;
    private long relativeMessageId;
    private long relativeChatId;
    private long chatId;
    private MessageType messageType;
    private String fullName;

    private long avatarId;

    private Date createdDate;
    private Time createdTime;

    public Message() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getChatId() {
        return chatId;
    }

    public Message setChatId(long chatId) {
        this.chatId = chatId;

        return this;
    }

    public long getAuthorID() {
        return authorID;
    }

    public void setAuthorID(long authorID) {
        this.authorID = authorID;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Message setText(String text) {
        this.text = text;

        return this;
    }

    public long getId() {
        return id;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Message setMessageType(MessageType messageType) {
        this.messageType = messageType;

        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public Message setFullName(String fullName) {
        this.fullName = fullName;

        return this;
    }

    public long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(long avatarId) {
        this.avatarId = avatarId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Message setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;

        return this;
    }

    public Time getCreatedTime() {
        return createdTime;
    }

    public Message setCreatedTime(Time createdTime) {
        this.createdTime = createdTime;

        return this;
    }
}
