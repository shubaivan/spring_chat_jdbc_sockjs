package com.spdu.model.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

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

    @Column(name = "relativeMessageId")
    private long relativeMessageId;

    @Column(name = "relativeChatId")
    private long relativeChatId;

    @Column(name = "attachmentFileId")
    private long attachmentFileId;

    public Message() {
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }

    public long getAttachmentFileId() {
        return attachmentFileId;
    }

    public void setAttachmentFileId(long attachmentFileId) {
        this.attachmentFileId = attachmentFileId;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getId() {
        return id;
    }
}
