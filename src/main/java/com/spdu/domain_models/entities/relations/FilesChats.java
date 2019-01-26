package com.spdu.domain_models.entities.relations;

public class FilesChats {
    private long id;
    private long fileId;
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
