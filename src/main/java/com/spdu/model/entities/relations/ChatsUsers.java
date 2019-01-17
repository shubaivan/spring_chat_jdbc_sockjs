package com.spdu.model.entities.relations;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chatsUsers")
public class ChatsUsers {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "chatId")
    private long chatId;

    @Column(name = "userId")
    private long userId;

    @Column(name = "dateOfJoined")
    private LocalDateTime dateOfJoined;

    public long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getDateOfJoined() {
        return dateOfJoined;
    }

    public void setDateOfJoined(LocalDateTime dateOfJoined) {
        this.dateOfJoined = dateOfJoined;
    }
}
