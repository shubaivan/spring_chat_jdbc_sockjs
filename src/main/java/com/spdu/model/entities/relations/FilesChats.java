package com.spdu.model.entities.relations;

import javax.persistence.*;

@Entity
@Table(name = "filesChats")
public class FilesChats {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "fileId")
    private long fileId;

    @Column(name = "chatId")
    private long chatId;

    public FilesChats() {

    }

    public long getId() {
        return id;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }
}
