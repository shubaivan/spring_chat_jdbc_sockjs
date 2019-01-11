package com.spdu.model.entities;

import java.time.LocalDateTime;

public class Message {
    private long id;
    private long chatId;
    private long userId;
    private String text;
    private String userName;
    private LocalDateTime dateOfCreated;
    private long relativeMessageId;
    private long relativeChatId;
    private long attachmentFileId;

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }
}
