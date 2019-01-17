package com.spdu.model.entities;

import com.spdu.model.constants.ChatType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "dateOfCreated")
    private LocalDateTime dateOfCreated;

    @Column(name = "chatType")
    private ChatType chatType;

    @Column(name = "tags")
    private String tags;

    @Column(name = "description")
    private String description;

    public Chat() {
        super();
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

    public LocalDateTime getDateOfCreated() {
        return dateOfCreated;
    }

    public void setDateOfCreated(LocalDateTime dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
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
