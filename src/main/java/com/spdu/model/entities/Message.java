package com.spdu.model.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "text")
    private String text;

    @Column(name = "dateOfCreated")
    private LocalDateTime dateOfCreated;

    @Column(name = "relativeMessageId")
    private long relativeMessageId;

    @Column(name = "relativeChatId")
    private long relativeChatId;

    @OneToMany(mappedBy = "message")
    private List<FavoriteMessage> favoriteMessages;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public List<FavoriteMessage> getFavoriteMessages() {
        return favoriteMessages;
    }

    public void setFavoriteMessages(List<FavoriteMessage> favoriteMessages) {
        this.favoriteMessages = favoriteMessages;
    }
}
