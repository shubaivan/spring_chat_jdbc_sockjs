package com.spdu.model.entities;

import java.time.LocalDateTime;

public class FavoriteMessage {
    private long id;
    private long chatId;
    private long userId;
    private String text;
    private String userName;
    private LocalDateTime dateOfCreated;
    private long messageId;
}
